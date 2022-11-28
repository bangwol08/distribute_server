package RMI;

import dao.rawDataDao;
import dao.resultDao;
import dto.rawDataDto;
import dto.resultDto;
import operation.Counting;
import operation.Filtering;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class RemoteServer extends UnicastRemoteObject implements RemoteInterface
{
    public RemoteServer() throws RemoteException
    {
        super();
    }

    public void startServer()
    {
        try
        {
            //RMI용 인터페이스를 구현한 원격객체 생성
            RemoteInterface inf = new RemoteServer();
            //구현한 객체를 클라이언트에서 찾을 수 있도록 Registry객체를 생성해서 등록
            Registry reg = LocateRegistry.createRegistry(1099);

            // Registry서버에 제공하는 객체 등록
            // 형식) Registry객체변수.rebind("객체Alias", 원격객체변수);
            reg.rebind("server", inf);

            System.out.println("Waiting for client...");
            System.out.println("----------------------------------------------");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void doRemoteWordCounting() throws RemoteException
    {
        ArrayList<rawDataDto> rawDataList; //rawDataList 담을 List
        ArrayList<String> preprocessedDataList; //전처리된 DataList 담을 List
        ArrayList<resultDto> resultDataList; //카운팅 결과를 저장 및 DB 전송용 List

        //DB에 연결해 rawDataList 받아옴
        rawDataDao rawData = new rawDataDao();
        rawDataList = rawData.importRawData();

        System.out.println("----------------------------------------------");
        System.out.println("Operation process to preprocess and count words");
        System.out.println("----------------------------------------------");

        //필터링 클래스로 rawDataList 전송 및 결과를 List로 받아옴
        Filtering filtering = new Filtering();
        preprocessedDataList = filtering.filter(rawDataList);

        //결과 받을 배열 생성 및 배열을 토대로 카운팅
        Counting counting = new Counting();
        resultDataList = counting.wordCounter(preprocessedDataList);

        System.out.println("----------------------------------------------");

        //최종DB 연결 및 word_count table에 저장
        resultDao resultDbConn = new resultDao();
        resultDbConn.insertResultList(resultDataList);

        System.out.println();
        System.out.println("---------------------End----------------------");
    }
}
