package com.example.moing.board;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.moing.R;

import java.util.ArrayList;
import java.util.List;

public class BoardMakeVote extends AppCompatActivity {

    RecyclerView recyclerView;
    Button btn_close, btn_erase_content, addContent, deleteContent, upload;
    CheckBox  anony, multi;
    EditText title, content;
    TextView titleCount, contentCount, tv_anony, tv_multi;
    List<Vote> voteList = new ArrayList<>();
    List<Vote> deleteVote = new ArrayList<>();
    private int addContentCount;
    private VoteAdapater voteAdapater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_make_vote);

        // 투표 만들기 취소 버튼 & 클릭 리스너
        btn_close = (Button) findViewById(R.id.btn_close);
        btn_close.setOnClickListener(closeClickListener);
        // 제목
        title = (EditText) findViewById(R.id.et_title);
        // 제목 글자 수
        titleCount = (TextView) findViewById(R.id.tv_titleCount);
        // 내용
        content = (EditText) findViewById(R.id.et_content);
        // 내용 글자 수
        contentCount = (TextView) findViewById(R.id.tv_contentCount);

        // 예정 : 항목지우기 클릭 리스너
        btn_erase_content = (Button) findViewById(R.id.btn_erase);
        btn_erase_content.setOnClickListener(contentEraseClickListener);
        btn_erase_content.setClickable(false);


        // 투표 항목 추가하기 & 리스너
        addContent = (Button) findViewById(R.id.imgbtn_add);
        addContent.setOnClickListener(addContentClickListener);
        addContentCount=0;

        // 선택한 항목 지우기
        deleteContent = (Button) findViewById(R.id.imgbtn_erase);
        deleteContent.setOnClickListener(deleteContentClickListener);

        // TextWatcher 등록
        setTextWatcher(title, titleCount, 15);
        setTextWatcher(content, contentCount, 300);

        recyclerView = findViewById(R.id.recycle_vote);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        // 리싸이클러뷰 스크롤 방지
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        linearLayoutManager.setAutoMeasureEnabled(true);

        // 리싸이클러뷰 어댑터 설정
        // adapter 설정
        voteAdapater = new VoteAdapater(voteList, this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(voteAdapater);
        recyclerView.setHasFixedSize(true);

        // 익명, 복수 투표
        anony = (CheckBox) findViewById(R.id.btn_anony);
        multi = (CheckBox) findViewById(R.id.btn_multi);
        tv_anony = (TextView) findViewById(R.id.tv_anony);
        tv_multi = (TextView) findViewById(R.id.tv_multi);
        anony.setOnClickListener(anonyClickListener);
        multi.setOnClickListener(multiClickListener);

        // 업로드 버튼
        upload = (Button) findViewById(R.id.btn_upload);
        upload.setOnClickListener(uploadClickListener);
        upload.setClickable(false);
    }

    // 취소 버튼
    View.OnClickListener closeClickListener = v -> {
        finish();
    };

    /** EditText 입력 시 입력된 글자 수 변경 **/
    private void setTextWatcher(EditText editText, TextView countTextView, int maxLength) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                String result = "(" + editable.length() + "/" + maxLength + ")";
                countTextView.setText(result);
                checkInputs();
            }
        });
    }

    /** 항목 추가하기 버튼 클릭 **/
    View.OnClickListener addContentClickListener = v -> {
        Log.d("CONTENTCLICKADD", String.valueOf(voteList.size()));

        Vote vote = new Vote();
        voteList.add(vote);
        addContentCount = voteList.size();
        addContent.setText("항목 추가하기("+addContentCount+"/10)");
        voteAdapater.notifyDataSetChanged();

        Log.d("CONTENTCLICKADD", String.valueOf(voteList.size()));

        if(voteList.size() >= 2) {
            btn_erase_content.setClickable(true);
            btn_erase_content.setTextColor(Color.parseColor("#F43A6F"));
        }
        checkInputs();
    };

    /** 선택한 항목 지우기 버튼 클릭 **/
    View.OnClickListener deleteContentClickListener = v -> {
        //Log.d("CONTENTCLICKDELETE1", String.valueOf(voteList.size()));
        // 선택 항목 삭제
        deleteVote = voteAdapater.getSelectedItems();
        //Log.d("CONTENTCLICKDELETE2", String.valueOf(deleteVote.size()));
        voteList.removeAll(deleteVote);
        //Log.d("CONTENTCLICKDELETE3", String.valueOf(voteList.size()));
        voteAdapater.notifyDataSetChanged();

        // 다시 항목 추가하기로 이동
        boolean isVisible = voteAdapater.isButtonVisible();
        voteAdapater.setButtonVisible(!isVisible);
        deleteContent.setVisibility(View.GONE);
        addContent.setVisibility(View.VISIBLE);
        addContentCount = voteList.size();
        addContent.setText("항목 추가하기("+addContentCount+"/10)");
        btn_erase_content.setText("항목 지우기");

        if (voteList.size() < 2) {
            btn_erase_content.setClickable(false);
            btn_erase_content.setTextColor(Color.parseColor("#333232"));
        }
        else {
            btn_erase_content.setClickable(true);
            btn_erase_content.setTextColor(Color.parseColor("#F43A6F"));
        }
    };

    /** 항목 지우기 버튼 클릭 **/
    View.OnClickListener contentEraseClickListener = v -> {
        boolean isVisible = voteAdapater.isButtonVisible();
        voteAdapater.setButtonVisible(!isVisible);

        // 선택한 항목 지우기로 이동
        if(!isVisible) {
            btn_erase_content.setText("돌아가기");
            btn_erase_content.setTextColor(Color.parseColor("#F6F6F6"));
            addContent.setVisibility(View.GONE);
            deleteContent.setVisibility(View.VISIBLE);
            deleteContent.setClickable(true);
            deleteContent.setTextColor(Color.parseColor("#F43A6F"));
        }
        // 항목 추가하기로 이동
        else {
            btn_erase_content.setTextColor(Color.parseColor("#F43A6F"));
            btn_erase_content.setText("항목 지우기");
            addContent.setVisibility(View.VISIBLE);
            deleteContent.setVisibility(View.GONE);
            addContentCount = voteList.size();
            addContent.setText("항목 추가하기("+addContentCount+"/10)");
            //addContent.setTextColor(Color.parseColor("#F1F1F1"));
        }
    };

    /** 익명 투표 **/
    View.OnClickListener anonyClickListener = v -> {
        if(anony.isChecked()) {
            anony.setBackgroundResource(R.drawable.board_checkbox_yes);
            tv_anony.setTextColor(Color.parseColor("#F6F6F6"));
        } else {
            anony.setBackgroundResource(R.drawable.board_checkbox_no);
            tv_anony.setTextColor(Color.parseColor("#959698"));
        }
    };

    /** 복수 투표 **/
    View.OnClickListener multiClickListener = v -> {
        if(multi.isChecked()) {
            multi.setBackgroundResource(R.drawable.board_checkbox_yes);
            tv_multi.setTextColor(Color.parseColor("#F6F6F6"));
        }
        else {
            multi.setBackgroundResource(R.drawable.board_checkbox_no);
            tv_multi.setTextColor(Color.parseColor("#959698"));
        }
    };

    /** 업로드하기 버튼 **/
    View.OnClickListener uploadClickListener = v -> {

    };

    public void checkInputs() {
        if(title.length() > 0 && content.length() > 0 && checkVoteContent())
        {
            upload.setClickable(true);
            upload.setTextColor(Color.parseColor("#202020"));
            upload.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
    }

    public boolean checkVoteContent() {
        List<Vote> votes = voteAdapater.getVoteList();
        if (votes.size() <= 0) {
            return false;
        }

        for (Vote vote : votes) {
            if(vote.getVoteContent().length() <= 0) {
                return false;
            }
        }
        return true;
    }
}