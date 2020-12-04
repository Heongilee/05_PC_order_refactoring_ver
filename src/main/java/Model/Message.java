package Model;

public class Message {
   private String seat; // 좌석 정보
   private String id; // 로그인한 아이디
   private String password; // 로그인한 패스워드
   private String msg; // 채팅 내용이나 서버에 보낼 메시지 정보
   private String type; // 모드
   private String receiveId; // 채팅 내용을 받을 사용자나 서버에 보낼 메시지 정보
   
   public Message() { // 메시지 객체를 초기화 해줍니다.
      this.seat = "";
      this.id = "";
      this.password = "";
      this.msg = "";
      this.type = "";
      this.receiveId = "";
   }
   public Message(String seat, String id, String password, String msg, String type, String receiveId) { // 메시지 정보가 들어오면 최신화 해줍니다.
      this.seat = seat;
      this.id = id;
      this.password = password;
      this.msg = msg;
      this.type = type;
      this.receiveId = receiveId;
   }
   public String getSeat() {
      return seat;
   }

   public void setSeat(String seat) {
      this.seat = seat;
   }
   
   public String getReceiveId() {
      return receiveId;
   }

   public void setReceiveId(String receiveId) {
      this.receiveId = receiveId;
   }

   public String getId() {
      return id;
   }
   public void setId(String id) {
      this.id = id;
   }
   public String getPassword() {
      return password;
   }
   public void setPassword(String password) {
      this.password = password;
   }
   public String getType() {
      return type;
   }
   public void setType(String type) {
      this.type = type;
   }
   public String getMsg() {
      return msg;
   }
   public void setMsg(String msg) {
      this.msg = msg;
   }
   @Override
   public String toString() {
      return "Message [id=" + id + ", password=" + password + ", type=" + type + ", msg=" + msg + "]";
   }
   
   
   
}