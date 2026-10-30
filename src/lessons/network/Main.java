package lessons.network;

import lessons.network.bsdsocks.TcpClient;
import lessons.network.bsdsocks.TcpFramingIrc;
import lessons.network.bsdsocks.TcpServer;
import lessons.network.bsdsocks.UdpClient;
import plm.core.model.lesson.Lesson;

public class Main extends Lesson {
  @Override protected void loadExercises()
  {
    addExercise(new TcpClient(this));
    addExercise(new TcpServer(this));
    addExercise(new TcpFramingIrc(this));
    addExercise(new UdpClient(this));
  }
}
