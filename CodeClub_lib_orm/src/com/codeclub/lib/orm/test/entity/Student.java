package com.codeclub.lib.orm.test.entity;

import android.graphics.Bitmap;

import com.codeclub.lib.orm.annotation.Column;
import com.codeclub.lib.orm.annotation.Column.ColumnType;
import com.codeclub.lib.orm.annotation.Table;

@Table(name="student")
public class Student {
	
	@Column(primaryKey=true, autoIncrease=true, type=ColumnType.INTEGER)
	public int id;
	
	@Column(name="username", type=ColumnType.TEXT)
	public String name;
	
	@Column(type=ColumnType.INTEGER)
	public int age;
	
	@Column(type=ColumnType.BLOB)
	public Bitmap avatar;
	
	@Column(type=ColumnType.TEXT)
	public String birthday;
	
}
