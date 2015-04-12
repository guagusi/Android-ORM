package com.codeclub.lib.orm.test.entity;

import java.util.List;

import android.graphics.Bitmap;

import com.codeclub.lib.orm.annotation.Column;
import com.codeclub.lib.orm.annotation.Column.ColumnType;
import com.codeclub.lib.orm.annotation.Table;

@Table(name="teacher")
public class Teacher {

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
	
	@Column(type=ColumnType.REAL)
	public double weigth;
	
	
	public double getWeigth() {
		return weigth;
	}

	public void setWeigth(double weigth) {
		this.weigth = weigth;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public Bitmap getAvatar() {
		return avatar;
	}

	public void setAvatar(Bitmap avatar) {
		this.avatar = avatar;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	
	
}
