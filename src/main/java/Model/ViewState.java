package Model;
import java.util.Iterator;
import java.util.Vector;

public class ViewState {
    private static Vector<String> viewStateList = null;
    private static ViewState viewState = null;
    private String current_view_state = "";

    private ViewState() {}

    public String getviewStateList(String viewName) {
        String result = null;
        Iterator it = viewStateList.iterator();
        while(it.hasNext()) {
            String current_item = (String) it.next();
            if(viewName.equals(current_item)) {
                result = current_item;
                
                break;
            }
        }
        if(result == null) {
            System.out.println("ERROR : View가 존재하지 않습니다.");
        }

        return result;
    }

    public String getCurrent_view_state() {
        return current_view_state;
	}

	public void setCurrent_view_state(String current_view_state) {
		this.current_view_state = current_view_state;
	}

	public static ViewState getInstance() {
        if(viewState == null) {
            viewState = new ViewState();

            if(viewStateList == null) {
                viewStateList = new Vector<String>();

                // View 파일명을 그대로 따라감.
                viewStateList.add("AdminView");
                viewStateList.add("CusManager");
                viewStateList.add("GUIView");
                viewStateList.add("LoginView");
                viewStateList.add("ProdManager");
                viewStateList.add("SignUpView");
            }
        }
        return viewState;
    }
}