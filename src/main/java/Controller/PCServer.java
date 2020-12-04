package Controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Logger;
import com.google.gson.Gson;
import Model.Message;
import Model.Orders_DAO;
import Model.Orders_DTO;

public class PCServer {
   // 서버 소켓 및 클라이언트 연결 소켓
   private ServerSocket ss = null;
   private Socket s = null;
   // 연결된 클라이언트 스레드를 관리하는 ArrayList
   ArrayList<ChatThread> chatThreads = new ArrayList<ChatThread>();
   int[] seatNum = new int[12];   
//   CusManager CM = CusManager.getInstance();
   // 로거 객체
   Logger logger;

   Map<String,String> current_count;
   
   public static Orders_DAO o_dao;
   public static Vector<Orders_DTO> PCorder_list = new Vector<Orders_DTO>();
   
   public void start() {
      logger = Logger.getLogger(this.getClass().getName());

      try {
         // 서버 소켓 생성
         ss = new ServerSocket(8888);
         logger.info("PC Server start");

         // 무한 루프를 돌면서 클라이언트 연결을 기다린다.
         while (true) {
            s = ss.accept();
            // 연결된 클라이언트에 대해 스레드 클래스 생성
            ChatThread chat = new ChatThread();
            // 클라이언트 리스트 추가
            chatThreads.add(chat);
            // 스레드 시작
            chat.start();
         } // while
      } catch (Exception e) {
         logger.info("[MultiChatServer]start() Exception 발생!!");
         e.printStackTrace();
      }

   } // start()

   class ChatThread extends Thread {
      // 수신 메시지 및 파싱 메시지 처리를 위한 변수 선언
      String msg;

      boolean status = true;

      // 메시지 객체 생성
      Message m = new Message();

      // JSON 파서 초기화
      Gson gson = new Gson();

      // 입출력 스트림
      private BufferedReader inMsg = null;
      private PrintWriter outMsg = null;
      
      public Map<String,String> now_current()
      {
        /*관리자를 제외한 사용자의 아이디와 좌석 정보를 key와 value 값으로 해쉬맵에 저장하여 사용자가 로그인시에 좌석 정보를 추가 시키기 위해 사용하는 메소드 입니다.*/
         Map<String,String> rs = new HashMap<String,String>();
         for(ChatThread chat :chatThreads)
         {
            if(!chat.m.getId().equals("관리자"))
            {
               rs.put(chat.m.getId(),chat.m.getSeat());
            }
         }
         return rs;
      }
      
      @Override
      public void run() {
         try {
            inMsg = new BufferedReader(new InputStreamReader(s.getInputStream()));
            outMsg = new PrintWriter(s.getOutputStream(), true);

            while (status) {
               msg = inMsg.readLine();
               m = gson.fromJson(msg, Message.class);
               if (m.getType().equals("logout")) {
                 /*로그아웃한 클라이언트의 스레드를 지우고 좌석정보를 메시지로 보냅니다.*/
                  chatThreads.remove(this);
                  int[] customerSeat = new int[12];
                  customerSeat = SeatCustomer();
                  msgSendToAdmin(gson.toJson(new Message(m.getSeat(), m.getId(), "", "님이 종료했습니다.", "user_logout", gson.toJson(customerSeat))));
                  seatNum[Integer.parseInt(m.getSeat())] = 0; // 로그아웃한 좌석을 0으로 초기화하여 빈자리 상태로 만들기 위함입니다.
                  status = false;
               } else if (m.getType().equals("login")) {
                 /*로그인시에 앞에 자리부터 비교하여 자리가 비어있으면 처음 만난 빈자리에 로그인 합니다.*/
                  int i = 0;
                  for(i=1; i<=12; i++) {
                     if(seatNum[i] == 0) {
                        seatNum[i] = i;
                        break;
                     }
                  }
                  m.setSeat(Integer.toString(seatNum[i]));
                  msgSendToAdmin(gson.toJson(new Message(m.getSeat(), m.getId(), "","님이 로그인했습니다.", "user_login", "")));
               } else if (m.getType().equals("adminlogin") ) {
                 /*관리자 로그인시에 현재 접속중인 사용자들의 정보를 좌석창에 최신화 시키기 위해 메세지를 보냅니다.*/
                  Map<String,String> current_count;
                  current_count = now_current();                  
                  Message msg = new Message(m.getSeat(),m.getId(),"",gson.toJson(current_count),"current_count","");
                  outMsg.println(gson.toJson(msg));
               } else if (m.getType().equals("sendtoadmin")) {
                 /*사용자가 관리자에게만 채팅을 하기 위한 메소드를 호출합니다.*/
                  msgSendToAdmin(msg);
               } else if (m.getType().equals("admin")) {
                  /*관리자가 사용자 전체에게 메세지를 보내기 위한 메소드를 호출합니다.*/
                  msgSendAll(msg);
               } else if (m.getType().equals("adminlogout")) {
                  /*관리자의 스레드를 종료시킵니다.*/
                  chatThreads.remove(this);
                  status = false;
               } else if (m.getType().equals("ModeAll")) {
                 /*관리자가 전체 메세지 모드일때 사용자 모두에게 메세지를 보내는 메소드를 호출합니다.*/
                  msgSendAll(msg);
               } else if (m.getType().equals("ModeSelect")) {
                 /*관리자가 지정한 사용자에게 메세지를 보내기 위한 메소드를 호출합니다.*/
                  msgSendToCustomer(msg);
               } else if (m.getType().equals("orderSendServer")){
                  /*주문 정보를 최신화하여 좌석 정보창에 뿌려주기 위해 DB에 있는 주문 정보를 가져와 주문 정보를 메시지로 보냅니다.*/
                   o_dao.getInstance().ORDERS_FUNC_2();
                  
               String str = "";
               for (int i = 0; i < PCorder_list.size(); i++) {
                  if(m.getId().equals(PCorder_list.get(i).getcNAME())) {
                     str += PCorder_list.get(i).getpNAME() + " " + PCorder_list.get(i).getoCNT() + "개\n";
                  }
               }
               int[] customerSeat = new int[12];
               customerSeat = SeatCustomer();
               orderSendToAdmin(gson.toJson(new Message(m.getSeat(), m.getId(), "",m.getMsg(), "fromServer_order", gson.toJson(str)))); //주문 정보를 관리자 채팅창에 띄워주기 위해 메시지를 보냅니다.
            } else {

            }
            }
         } catch (Exception e) {
            e.printStackTrace();
         } finally {
            // 루프를 벗어나면 클라이언트 연결이 종료되므로 스레드 인터럽트
            this.interrupt();
            logger.info(this.getName() + " 종료됨!!");
         }
      }
      
      void orderSendToAdmin(String msg) {
         /*주문 정보를 관리자 채팅창에 띄워주기 위한 메소드 입니다.*/
         for (ChatThread ct : chatThreads) {
            if (ct.m.getId().equals("관리자")) {
               ct.outMsg.println(msg);
            }
         }
      }
      void msgSendToAdmin(String msg) {
        /*관리자에게만 채팅을 보내기 위한 메소드 입니다.*/
         outMsg.println(msg);
         for (ChatThread ct : chatThreads) {
            if (ct.m.getId().equals("관리자")) {
               ct.outMsg.println(msg);
            }
         }
      }
      void msgSendAll(String msg) {
        /*채팅 내용을 현재 접속한 사용자들에게 보내기 위한 메소드 입니다.*/
         for(ChatThread ct : chatThreads) {
            ct.outMsg.println(msg);
         }
      }
      void msgSendToCustomer(String msg) {
        /*지정한 사용자에게만 채팅을 보내기 위한 메소드 입니다.*/
         outMsg.println(msg);
         for (ChatThread ct : chatThreads) {
            if (ct.m.getId().equals(m.getReceiveId())) {
               ct.outMsg.println(msg);
            }
         }
      }
   int[] SeatCustomer() {
      /*좌석 정보를 알려주기 위한 메소드 입니다.*/
      int[] cnt = new int[12];
      for (ChatThread ct : chatThreads) {
         if (!ct.m.getId().equals("관리자")) {
            if (!ct.m.getId().equals("0"))
               cnt[Integer.parseInt(ct.m.getSeat()) - 1] = Integer.parseInt(ct.m.getSeat());
         }
      }
      return cnt;
   }
   }
   public static void main(String[] args) {
      PCServer server = new PCServer();
      server.start();
   }
}