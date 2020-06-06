package com.example.atchui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Noti_newFragment extends Fragment implements Noti_RecyclerAdapter.OnListItemSelectedInterface {
    private static final int CURRENT_NOTIFICATION = 1;
    private static final int PATH_NOTIFICATION = 2;

    private Noti_newRecyclerAdapter new_adapter;
    private RecyclerView new_recyclerView;

    private SharedViewModel sharedViewModel; //fragment간 text전달을 위해 만듬

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_noti_new_fragment, container, false);

        Initialize(view);
        setData();

//        //"새로운 알림" textView Visible, Invisible하게 만드는 코드(나중에 item insert, delete 하는 함수에 넣을 것)
//        //View view2 = getLayoutInflater().inflate(R.layout.activity_notification_list, container, false);
//        LayoutInflater layoutInflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View view2 = layoutInflater.inflate(R.layout.activity_notification_list, null);
//
//        TextView textView_newnoti = (TextView)view2.findViewById(R.id.textview_newNoti);
//        if(new_adapter.getItemCount() == 0){
////            textView_newnoti.setVisibility(View.INVISIBLE);
//            Log.d("newText", textView_newnoti.getText()+" ");
//        }else{
////            textView_newnoti.setVisibility(View.VISIBLE);
//            Log.d("newText", textView_newnoti.getText()+" ");
//        }

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        sharedViewModel = ViewModelProviders.of(getActivity()).get(SharedViewModel.class); //fragment간 text전달을 위해 만듬
    }

    private void Initialize(View view) {

        new_recyclerView = view.findViewById(R.id.recycler_newnoti);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        new_recyclerView.setLayoutManager(linearLayoutManager);

        new_adapter = new Noti_newRecyclerAdapter(this);
        new_recyclerView.setAdapter(new_adapter);

    }
    private void setData() {
        //ItemType: CURRENT_NOTIFICATION: 현위치 알림, PATH_NOTIFICATION: 지난경로 알림

//        Noti_RecyclerItem item = new Noti_RecyclerItem();
//
//        item.setItemType(CURRENT_NOTIFICATION);
//        item.setLabelColor(this.getResources().getColor(R.color.label_green));
//        item.setTextStr("반경 1km 내에 확진자 동선이 확인되었습니다.");
//        item.setTimeStr("1분 전");
//
//        new_adapter.addItem(item);
//
//        item = new Noti_RecyclerItem();
//        item.setItemType(PATH_NOTIFICATION);
//        item.setLabelColor(this.getResources().getColor(R.color.label_red));
//        item.setTextStr("강남구청 근방에서 2020-05-28에 동선겹침이 확인되었습니다.");
//        item.setTimeStr("1분 전");
//
//        new_adapter.addItem(item);

        new_adapter.notifyDataSetChanged();
    }
    @Override
    public void onItemSelected(View v, int position) {

        Noti_newRecyclerAdapter.Noti_newItemViewHolder viewHolder =
                (Noti_newRecyclerAdapter.Noti_newItemViewHolder)new_recyclerView.findViewHolderForAdapterPosition(position);


        //itemType에 맞게 activity 이동
        if(viewHolder.itemType == 1){
            Intent intent = new Intent(getActivity(), CurrentResultActivity.class);

            //데이터(position) 송신 - 실제 포지션에 맞는 정보 출력할 때 사용
            intent.putExtra("position", position);

            startActivity(intent);
        }
        else if(viewHolder.itemType == 2){
            Intent intent = new Intent(getActivity(), PathResultActivity.class);

            //데이터(position) 송신
            intent.putExtra("position",position);

            startActivity(intent);
        }

        //fragment간 text전달을 위해 만듬
        if(sharedViewModel!=null){
            int itemType = viewHolder.itemType;
            ColorDrawable drawable = (ColorDrawable) viewHolder.labelColor.getBackground();
            drawable.getColor();
            int labelColor = drawable.getColor();
            String contentStr = viewHolder.textContent.getText().toString();
            String timeStr = viewHolder.textTime.getText().toString();
            Noti_RecyclerItem item = new Noti_RecyclerItem(itemType, labelColor, contentStr, timeStr);
            sharedViewModel.setItem(item);
        }
    }
}