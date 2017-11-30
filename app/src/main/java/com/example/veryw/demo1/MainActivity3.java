package com.example.veryw.demo1;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.view.Gravity;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class MainActivity3 extends Activity {

	DrawerLayout mDrawerLayout;
	Button mButton;
	ListView mDrawerList;
	String[] str={"AA","BB","CC","DD","EE","FF"};
	RelativeLayout mRelativeLayout;
	int mDrawerWidth;
	float scrollWidth;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mlayout);

		
		mDrawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);
		mRelativeLayout=(RelativeLayout) findViewById(R.id.content_frame);
		
		mDrawerList=(ListView)findViewById(R.id.left_drawer);
		mDrawerList.setAdapter(new ArrayAdapter<String>(getApplicationContext(),
				android.R.layout.simple_expandable_list_item_1,str));
		measureView(mDrawerList);
		mDrawerWidth=mDrawerList.getMeasuredWidth();
		
		mButton=(Button)findViewById(R.id.mButton);
		mButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mDrawerLayout.openDrawer(Gravity.LEFT);
			}
		});
			
		mDrawerLayout.setDrawerListener(new DrawerListener() {
			
			@Override
			public void onDrawerStateChanged(int arg0) {
				
			}
			
			@Override
			public void onDrawerSlide(View arg0, float arg1) {
				
				scrollWidth=arg1*mDrawerWidth;
				mRelativeLayout.setScrollX((int)(-1*scrollWidth));
				
			}
			
			@Override
			public void onDrawerOpened(View arg0) {
				
			}
			
			@Override
			public void onDrawerClosed(View arg0) {
				
			}
		});

	}
	

	private void measureView(View child) {    
        ViewGroup.LayoutParams params = child.getLayoutParams();    
        if (params == null) {    
            params = new ViewGroup.LayoutParams(    
                    ViewGroup.LayoutParams.MATCH_PARENT,    
                    ViewGroup.LayoutParams.WRAP_CONTENT);    
        }    
        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0,    
                params.width);    
        int lpHeight = params.height;    
        int childHeightSpec;    
        if (lpHeight > 0) {    
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,    
                    MeasureSpec.EXACTLY);    
        } else {    
            childHeightSpec = MeasureSpec.makeMeasureSpec(0,    
                    MeasureSpec.UNSPECIFIED);    
        }    
        child.measure(childWidthSpec, childHeightSpec);    
    }    
	

}
