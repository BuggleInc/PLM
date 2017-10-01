/**
 *  This program recompiles the judge (by calling "ant dist"), 
 *  and then makes sure that exactly CHILDCOUNT judge are running at the same time.
 *  As soon as one of the childs exits, another one is fork+exec()ed.
 *  When the program is exited with Ctrl-C, it terminates all started judges before leaving.
 * 
 *  This probably helps to face a moderate load (such as the one experienced for several dozen of human users), 
 *  but it is perfectly useless when facing a really high load (such as the one experienced when running all 
 *  tests back to back).
 * 
 *  This is because a judge needs 2 to 7 seconds to start, load all involved classes, and warmup the embeeded 
 *  compilers while most exercises run in less than 150ms. So, there is a 1/200 ratio between the duration where
 *  the judge is actually useful, and the time it takes to boot up.
 * 
 *  Having a bunch of judges already started helps to handle load bursts, but cannot help against a
 *  continuously high load. Several ideas to push the performance further:
 *   - checkpoint the JVM once the judge is initialized and all compilers warm
 *   - find a better way to sandbox the learner code than restarting the JVM completely.
 */

#include <stdio.h>
#include <stdlib.h>
#include <signal.h>
#include <sys/types.h>
#include <unistd.h>
#include <sys/wait.h>

#define CHILDCOUNT 4
#define DELAY 8 /* inter-child delay during the warmup */

int pids[CHILDCOUNT];

void runchild() {
   execlp("java", "java", "-cp", "dist/plm-2.6-20151010.jar:.:bin:lib/rabbitmq-client.jar", "plm.judge.Main", NULL);
   perror("execlp java failed");
   exit(1);   
}

int armagedon = 0;
static void inthandler(int ignored) {
   armagedon = 1;
   printf("Terminate everyone...\n");
   for (int i=0; i<CHILDCOUNT; i++)
      kill(pids[i], SIGTERM);
   sleep(5);
   printf("Kill everyone...\n");
   for (int i=0; i<CHILDCOUNT; i++)
      kill(pids[i], SIGKILL);
   sleep(5);
   exit(1);
}

int main(int argc, char* argv[]) {
   
   pid_t pid;
   
   printf("Rebuild the jarfile\n");
   pid = fork();
   if (pid==0) {// child
      execlp("ant", "ant", "dist", NULL);
      perror("execlp ant failed");
      exit(1);   
   }
   wait(NULL);
   
   printf("Start %d childs, and keep the pool full\n", CHILDCOUNT);
   for (int i=0; i<CHILDCOUNT; i++){
      pids[i] = fork();
      if (pids[i] == 0) 
	runchild();
      printf(" launch child #%d\n",i);
      fflush(stdout);
      sleep(DELAY);
   }
   printf("All childs are launched\n");
   
   /* Kill everyone on Ctrl-C */
   signal(SIGINT, inthandler);
   
   while (1) {
      int status;
      pid = wait(&status);
      if (!armagedon)
	for (int i=0; i<CHILDCOUNT; i++)
	  if (pids[i] == pid) {
	     pids[i] = fork();
	     if (pids[i] == 0)
	       runchild();
	  }      
   }
}
