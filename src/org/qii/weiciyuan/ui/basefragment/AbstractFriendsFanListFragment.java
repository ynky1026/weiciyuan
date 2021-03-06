package org.qii.weiciyuan.ui.basefragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.AbsListView;
import org.qii.weiciyuan.bean.UserBean;
import org.qii.weiciyuan.bean.UserListBean;
import org.qii.weiciyuan.ui.interfaces.AbstractAppActivity;
import org.qii.weiciyuan.ui.interfaces.IUserInfo;

import java.util.List;

/**
 * User: qii
 * Date: 12-11-10
 */
public abstract class AbstractFriendsFanListFragment extends AbstractUserListFragment {

    protected UserBean currentUser;
    protected String uid;


    public AbstractFriendsFanListFragment() {

    }

    public AbstractFriendsFanListFragment(String uid) {
        this.uid = uid;
    }

    @Override
    protected void oldUserOnPostExecute(UserListBean newValue) {
        if (newValue != null && newValue.getUsers().size() > 0 && newValue.getPrevious_cursor() != bean.getPrevious_cursor()) {
            List<UserBean> list = newValue.getUsers();
            getList().getUsers().addAll(list);
            bean.setNext_cursor(newValue.getNext_cursor());
            buildActionBarSubtitle();
        }

    }

    @Override
    protected void newUserOnPostExecute() {
        buildActionBarSubtitle();
    }

    private void buildActionBarSubtitle() {
        if (!TextUtils.isEmpty(currentUser.getFriends_count())) {

            int size = Integer.valueOf(currentUser.getFriends_count());
            int newSize = bean.getTotal_number();
            String number = "";
            if (size >= newSize) {
                number = bean.getUsers().size() + "/" + size;
            } else {
                number = bean.getUsers().size() + "/" + newSize;
            }
            getActivity().getActionBar().setSubtitle(number);
        }
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        commander = ((AbstractAppActivity) getActivity()).getCommander();
        if (savedInstanceState != null) {
            currentUser = (UserBean) savedInstanceState.getSerializable("currentUser");
            uid = savedInstanceState.getString("uid");
            clearAndReplaceValue((UserListBean) savedInstanceState.getSerializable("bean"));
            getAdapter().notifyDataSetChanged();

        } else {
            pullToRefreshListView.startRefreshNow();

        }

        refreshLayout(bean);

        getListView().setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentUser = ((IUserInfo) getActivity()).getUser();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("currentUser", currentUser);
        outState.putString("uid", uid);
    }
}
