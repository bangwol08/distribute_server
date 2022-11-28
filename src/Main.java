import RMI.RemoteServer;
import dao.resultDao;

public class Main {
    public static void main(String[] args){
        System.out.println("----------------------Start-------------------");
        try
        {
            //서버 시작
            RemoteServer server = new RemoteServer();
            server.startServer();
        }
        catch(Exception e)
        {
            System.out.println("Client/Server connection Error");
            e.printStackTrace();
        }
    }

}

