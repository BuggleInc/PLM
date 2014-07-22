
FILE* f;

int main(int argc, char* argv[]){
	f = fopen(argv[1],"w+");
	if(!f){
		perror("Error open temp file");
		return -1;
	}
	run();
	fclose(f);
	return 0;
}



void myPrintf(char* format, ...){
	va_list args;
   	va_start(args, format);
   	vfprintf(f,format, args);
   	fprintf(f,"\n");
   	fflush(f);
   	va_end(args);
}

void flush(){
    int c = 0;
    FILE* f = fopen("/tmp/flush","a+");
    while (c != '\n' && c != EOF){
        c = getchar();
        fputc(c,f);
    }
    fclose(f);
}