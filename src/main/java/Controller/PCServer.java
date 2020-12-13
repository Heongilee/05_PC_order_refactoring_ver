package Controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import com.google.gson.Gson;
import Model.Message;
import Model.OrdersDao;

public class PCServer {
	// 서버 소켓 및 클라이언트 연결 소켓
	private ServerSocket _serverSocket = null;
	private Socket _socket = null;

	// 연결된 클라이언트 스레드를 관리하는 ArrayList
	ArrayList<UserThread> _userThreads = new ArrayList<UserThread>();
	final int _totalSeat = 13;
	int[] _seatNumber = new int[_totalSeat];

	// 로거 객체
	Logger logger;

	Map<String, String> currentCount;

	public static OrdersDao _ordersDao = OrdersDao.getInstance();
	//! This field has been depricated...
	// public static Vector<OrdersDto> PCorder_list = new Vector<OrdersDto>();

	public void start() {
		logger = Logger.getLogger(this.getClass().getName());

		try {
			// 서버 소켓 생성
			_serverSocket = new ServerSocket(8888);
			logger.info("PC Server start");

			// 무한 루프를 돌면서 클라이언트 연결을 기다린다.
			while (true) {
				System.out.println("서버 대기중...");
				_socket = _serverSocket.accept();

				// 연결된 클라이언트에 대해 스레드 클래스 생성
				UserThread user = new UserThread(_socket);
				_userThreads.add(user);
				user.start();
			}
		} catch (Exception e) {
			logger.info("[MultiChatServer]start() Exception 발생!!");
			e.printStackTrace();
		}
	} // start()

	public static void main(String[] args) {
		System.out.println("서버 가동");
		PCServer server = new PCServer();
		server.start();
	}

	class UserThread extends Thread {
		// 수신 메시지 및 파싱 메시지 처리를 위한 변수 선언
		String msg;
		boolean status = true;

		// 메시지 객체 생성
		Message msgObject = new Message();

		// JSON 파서 초기화
		Gson gson = new Gson();

		// 입출력 스트림
		private BufferedReader inMsg = null;
		private PrintWriter outMsg = null;

		public UserThread(Socket socket) {
			try {
				inMsg = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				outMsg = new PrintWriter(socket.getOutputStream(), true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public Map<String, String> getCurrentSeat() {
			/*
			 * 관리자를 제외한 사용자의 아이디와 좌석 정보를 key와 value 값으로 해쉬맵에 저장하여 사용자가 로그인시에 좌석 정보를 추가 시키기
			 * 위해 사용하는 메소드 입니다.
			 */
			Map<String, String> temp = new HashMap<String, String>();
			for (UserThread user : _userThreads) {
				if (!user.msgObject.getId().equals("관리자")) {
					temp.put(user.msgObject.getId(), user.msgObject.getSeat());
				}
			}
			return temp;
		}

		@Override
		public void run() {
			try {
				while (status) {
					msg = inMsg.readLine();
					msgObject = gson.fromJson(msg, Message.class);
					if (checkMode("logout")) {
						/* 로그아웃한 클라이언트의 스레드를 지우고 좌석정보를 메시지로 보냅니다. */
						_userThreads.remove(this);
						int[] customerSeat = new int[_totalSeat];
						customerSeat = getSeatNumber();
						msgSendToAdmin(gson.toJson(new Message(msgObject.getSeat(), msgObject.getId(), "", "님이 종료했습니다.",
								"user_logout", gson.toJson(customerSeat))));
						_seatNumber[Integer.parseInt(msgObject.getSeat())] = 0; // 로그아웃한 좌석을 0으로 초기화하여 빈자리 상태로 만들기
																				// 위함입니다.
						status = false;
					} else if (checkMode("login")) {
						/* 로그인시에 앞에 자리부터 비교하여 자리가 비어있으면 처음 만난 빈자리에 로그인 합니다. */
						fillSeatbySequential();
						msgSendToAdmin(gson.toJson(new Message(msgObject.getSeat(), msgObject.getId(), "",
								"님이 로그인했습니다.", "user_login", "")));
					} else if (checkMode("adminlogin")) {
						/* 관리자 로그인시에 현재 접속중인 사용자들의 정보를 좌석창에 최신화 시키기 위해 메세지를 보냅니다. */
						Message msg = new Message(msgObject.getSeat(), msgObject.getId(), "",
								gson.toJson(getCurrentSeat()), "current_count", "");
						outMsg.println(gson.toJson(msg));
					} else if (checkMode("sendtoadmin")) {
						/* 사용자가 관리자에게만 채팅을 하기 위한 메소드를 호출합니다. */
						msgSendToAdmin(msg);
					} else if (checkMode("admin")) {
						/* 관리자가 사용자 전체에게 메세지를 보내기 위한 메소드를 호출합니다. */
						msgSendAll(msg);
					} else if (checkMode("adminlogout")) {
						/* 관리자의 스레드를 종료시킵니다. */
						_userThreads.remove(this);
						status = false;
					} else if (checkMode("ModeAll")) {
						/* 관리자가 전체 메세지 모드일때 사용자 모두에게 메세지를 보내는 메소드를 호출합니다. */
						msgSendAll(msg);
					} else if (checkMode("ModeSelect")) {
						/* 관리자가 지정한 사용자에게 메세지를 보내기 위한 메소드를 호출합니다. */
						msgSendToCustomer(msg);
					} else if (checkMode("orderSendServer")) {
						//! This code has been depricated...
						/*while(!_ordersDao.getOrderQueue().isEmpty()) {
							OrdersDto ordersDto = _ordersDao.getOrderQueue().remove();
							if(msgObject.getId().equals(ordersDto.getcNAME())) {
								str += ordersDto.getpNAME() + " " + ordersDto.getoCNT() + "개\n";
							}
						}
						*/
						//주문 정보를 최신화하여 좌석 정보창에 뿌려주기 위해 DB에 있는 주문 정보를 가져와 주문 정보를 메시지로 보냅니다.
						_ordersDao.ORDERS_FUNC_2();
						
						String str = "";
						for (int i = 0; i < _ordersDao.getOrderList().size(); i++) {
							if (msgObject.getId().equals(_ordersDao.getOrderList().get(i).getcNAME())) {
								str += _ordersDao.getOrderList().get(i).getpNAME() + " " + _ordersDao.getOrderList().get(i).getoCNT() + "개\n";
							}
						}
						orderSendToAdmin(gson.toJson(new Message(msgObject.getSeat(), msgObject.getId(), "",
								msgObject.getMsg(), "fromServer_order", gson.toJson(str)))); // 주문 정보를 관리자 채팅창에 띄워주기 위해
																								// 메시지를 보냅니다.
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

		boolean checkMode(String str) {
			return msgObject.getType().equals(str);
		}

		void fillSeatbySequential() {
			int i = 0;
			for (i = 1; i <= _totalSeat; i++) {
				if (_seatNumber[i] == 0) {
					_seatNumber[i] = i;
					break;
				}
			}
			msgObject.setSeat(Integer.toString(_seatNumber[i]));
		}

		void orderSendToAdmin(String msg) {
			/* 주문 정보를 관리자 채팅창에 띄워주기 위한 메소드 입니다. */
			sendMessageToType(msg, "관리자");
		}

		void msgSendToAdmin(String msg) {
			/* 관리자에게만 채팅을 보내기 위한 메소드 입니다. */
			outMsg.println(msg);
			sendMessageToType(msg, "관리자");
		}

		void msgSendAll(String msg) {
			/* 채팅 내용을 현재 접속한 사용자들에게 보내기 위한 메소드 입니다. */
			sendMessageToType(msg, "");
		}

		void msgSendToCustomer(String msg) {
			/* 지정한 사용자에게만 채팅을 보내기 위한 메소드 입니다. */
			outMsg.println(msg);
			sendMessageToType(msg, msgObject.getReceiveId());
		}

		int[] getSeatNumber() {
			/* 좌석 정보를 알려주기 위한 메소드 입니다. */
			int[] temp = new int[_totalSeat];
			for (UserThread user : _userThreads) {
				if (!user.msgObject.getId().equals("관리자")) {
					temp[Integer.parseInt(user.msgObject.getSeat())] = Integer.parseInt(user.msgObject.getSeat());
				}
			}
			return temp;
		}

		void sendMessage(UserThread user, String msg) {
			user.outMsg.println(msg);
		}

		void sendMessageToType(String msg, String type) {
			for (UserThread user : _userThreads) {
				if (type == "") {
					sendMessage(user, msg);
				} else if (user.msgObject.getId().equals(type)) {
					sendMessage(user, msg);
				}
			}
		}

	}
}