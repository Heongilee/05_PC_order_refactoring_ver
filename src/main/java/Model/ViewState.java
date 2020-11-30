package Model;

import java.util.Iterator;
import java.util.Vector;

public class ViewState {
    // private static Vector<String> viewStateList = null;
    private static ViewState _viewState = null;
    private String currentViewState = "";

    private ViewState() {
    }

    // ! This code has been deprecated...
    /*
     * public String getviewStateList(String viewName) { String result = null;
     * Iterator it = viewStateList.iterator(); while(it.hasNext()) { String
     * current_item = (String) it.next(); if(viewName.equals(current_item)) { result
     * = current_item;
     * 
     * break; } } if(result == null) {
     * System.out.println("ERROR : View가 존재하지 않습니다."); }
     * 
     * return result; }
     */

    public String getCurrentViewState() {
        return currentViewState;
    }

    public void setCurrent_view_state(String current_view_state) {
        this.currentViewState = current_view_state;
    }

    public static ViewState getInstance() {
        if (_viewState == null) {
            _viewState = new ViewState();

            // ! This code has been deprecated...
            /* // View 파일명을 그대로 따라감.
            if(viewStateList == null) {
            viewStateList = new Vector<String>();

            viewStateList.add("AdminView");
            viewStateList.add("CusManager");
            viewStateList.add("GUIView");
            viewStateList.add("LoginView");
            viewStateList.add("ProdManager");
            viewStateList.add("SignUpView");
            }
            */
        }
        return _viewState;
    }
}