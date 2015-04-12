package com.codeclub.lib.orm.test;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.codeclub.lib.orm.ORMLoader;
import com.codeclub.lib.orm.R;
import com.codeclub.lib.orm.test.entity.Teacher;

public class MainActivity extends ActionBarActivity {

	private static final String TAG = MainActivity.class.getSimpleName();
	
	private ORMLoader mOrm;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mOrm = ORMLoader.getORMInstance(this);
		
		testInsert();
		testUpdate();
		testQuery();
	}
	
	private List<Teacher> teachers;

	public void testInsert() {
		teachers = new ArrayList<Teacher>();
		for(int i = 0; i < 1000; i ++) {
			Teacher teacher = new Teacher();
			teacher.setAge(10);
			teacher.setName("cao nima XO");
			teachers.add(teacher);
		}
		
		long beginTime = System.currentTimeMillis();
		mOrm.insertEntityList(teachers);
		long endTime = System.currentTimeMillis();
		Log.e("insert time", endTime - beginTime + "" + "ms " + "data size " + teachers.size());
		
		
	}
	
	public void testQuery() {
		long beginTime = System.currentTimeMillis();
		List<Teacher> result = null;
		Teacher teacher = teachers.get(0);
		teacher.setBirthday("yyyy-MM-dd");
		teacher.setWeigth(1002.3);
		try {
			result = mOrm.queryEntityList(teacher, Teacher.class.getField("weigth"));
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
		long endTime = System.currentTimeMillis();
		int size = result == null ? 0 : result.size();
		Log.e("query time", endTime - beginTime + "ms " + "data size " + size);
	}
	
	public void testDelete() {
		try {
			mOrm.deleteEntity(teachers.get(0), Teacher.class.getField("weigth"));
		} catch (NoSuchFieldException e1) {
			e1.printStackTrace();
		}
	}
	
	public void testUpdate() {
		Teacher teacher = teachers.get(0);
		teacher.setBirthday("yyyy-MM-dd");
		teacher.setWeigth(1002.3);
		try {
			mOrm.updateEntity(teacher, Teacher.class.getField("age"));
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
	}
}
