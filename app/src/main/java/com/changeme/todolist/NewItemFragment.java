package com.changeme.todolist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.speech.RecognizerIntent;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.changeme.todolist.model.ToDoTask;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Pattern;

public class NewItemFragment extends Fragment {
    private static final String TASK_INFO_SPLITER=" ";
    private static final String TASK_TAG_INDITATOR="#";
    private static final String TASK_HABBIT_INDITATOR="-1";

    public final static int VOICE_RECOGNITION=1;
    public EditText addNewItemText;
    private ImageView speechButton;
    private Button submitBtn;

    private OnAddNewItemListener mListener;

    public interface OnAddNewItemListener {
        public void onAddNewItem(ToDoTask itemName);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.new_item, container, false);
        addNewItemText=(EditText)view.findViewById(R.id.add_new_item);
        speechButton=(ImageView) view.findViewById(R.id.speechButton);
        submitBtn=(Button)view.findViewById(R.id.new_item_submit_btn);

        addNewItemText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction()==KeyEvent.ACTION_DOWN){
                    if(keyCode==KeyEvent.KEYCODE_DPAD_CENTER || keyCode==KeyEvent.KEYCODE_ENTER ) {
                        submitText();
                        return true;
                    }
                }
                return false;
            }
        });

        speechButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent speechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                speechIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "请说话");
                speechIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
                speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.CHINESE);
                startActivityForResult(speechIntent, VOICE_RECOGNITION);
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitText();
                speechButton.setVisibility(View.VISIBLE);
                submitBtn.setVisibility(View.GONE);
            }
        });

        return view;
    }

    private void submitText(){
        String newItem=addNewItemText.getText().toString();
        ToDoTask newTask=formatInput(newItem);
        mListener.onAddNewItem(newTask);
        addNewItemText.setText("");
    }

    private ToDoTask formatInput(String newTaskStr){
        ToDoTask task=new ToDoTask();
//        Pattern pattern = Pattern.compile("^#.*/s[.*]/s[/-?[/d]+]$");
        if(!newTaskStr.isEmpty()){
            String[] taskInfo=newTaskStr.split(TASK_INFO_SPLITER);
            task.setName(newTaskStr);
            task.setTodayIsDo(0);
            task.setCompleted(0);

            DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
            dateFormat.format(System.currentTimeMillis());
            task.setCreateDate(dateFormat.format(System.currentTimeMillis()));
            task.setDuringDays(0);
            task.setInterruptedDays(0);
            if(taskInfo.length!=2){
                task.setPlanDays(1);
            }else {
                if(taskInfo[1].equals(TASK_HABBIT_INDITATOR)) {
                    task.setHabbit(1);
                    task.setPlanDays(0);
                }else{
                    try {
                        task.setPlanDays(Integer.parseInt(taskInfo[1]));
                    }catch (NumberFormatException e){
                        task.setPlanDays(1);
                    }
                }
            }
            return  task;
        }else {
            return null;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnAddNewItemListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==VOICE_RECOGNITION &&resultCode==getActivity().RESULT_OK){
            ArrayList<String> results;
            results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String result=results.isEmpty()?"" : results.get(0);
            addNewItemText.setText(result);
            speechButton.setVisibility(View.GONE);
            submitBtn.setVisibility(View.VISIBLE);
        }
    }

}
