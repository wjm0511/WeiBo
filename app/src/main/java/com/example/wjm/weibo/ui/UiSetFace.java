package com.example.wjm.weibo.ui;

import java.util.ArrayList;
import java.util.HashMap;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.example.wjm.weibo.R;
import com.example.wjm.weibo.base.BaseMessage;
import com.example.wjm.weibo.base.BaseUiAuth;
import com.example.wjm.weibo.base.C;
import com.example.wjm.weibo.list.GridImageList;
import com.example.wjm.weibo.model.Image;

public class UiSetFace extends BaseUiAuth {
	
	GridView faceGridView = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_face);

		// get face image list
		this.doTaskAsync(C.task.faceList, C.api.faceList,null);
	}
	
	private void doSetFace (String faceId) {
		HashMap<String, String> urlParams = new HashMap<String, String>();
		urlParams.put("key", "face");
		urlParams.put("val", faceId);
		doTaskAsync(C.task.customerEdit, C.api.customerEdit, urlParams);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////
	// async task callback methods
	
	@Override
	public void onTaskComplete(int taskId, BaseMessage message) {
		super.onTaskComplete(taskId, message);
		switch (taskId) {
			case C.task.faceList:
				try {
					final ArrayList<Image> imageList = (ArrayList<Image>) message.getResultList("Image");
					final ArrayList<String> imageUrls = new ArrayList<String>();
					for (int i = 0; i < imageList.size(); i++) {
						Image imageItem = imageList.get(i);
						imageUrls.add(imageItem.getUrl());
					}
					faceGridView = (GridView) this.findViewById(R.id.app_face_grid);
					faceGridView.setAdapter(new GridImageList(this, imageUrls));
					faceGridView.setOnItemClickListener(new OnItemClickListener(){
						@Override
						public void onItemClick(AdapterView<?> parent, View view,
								int position, long id) {
							Image face = imageList.get(position);
							customer.setFace(face.getId());
							doSetFace(face.getId());
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
					toast(e.getMessage());
				}
				break;
			case C.task.customerEdit:
				toast("face has changed");
				doFinish();
				break;
		}
	}
	
	@Override
	public void onNetworkError (int taskId) {
		super.onNetworkError(taskId);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////
	// other methods
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}
	
}