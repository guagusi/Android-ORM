# Android-ORM
===============
通过注解反射实现，性能一般。
插入1000条数据耗时14200ms
查询12000条数据耗时4700ms

使用介绍
---------
  在项目资源目录res/values 下新建orm.xml，定义数据库的名字，版本号，数据表实体的全限定名。/<br>
    ```
    <?xml version="1.0" encoding="utf-8"?>
    <resources>
     
        <!-- 数据库名,name固定 -->
        <string name="orm_db_name">orm_test</string>
        <!-- 数据版本号，name固定 -->
        <string name="orm_db_version">7</string>
        <!-- 数据表，name固定 -->
        <string-array name="tables">
            <!-- 全限定名 -->
            <item name="student">com.codeclub.lib.orm.test.entity.Student</item>
            <item name="teacher">com.codeclub.lib.orm.test.entity.Teacher</item>
        </string-array>
    </resources>
    ```
  给实体添加注解
  ```
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
  ```
  
  通过ORMLoader单例调用实现怎删改查
  ```
  ORMLoader mOrm = ORMLoader.getORMInstance(this);
  mOrm.insertEntityList(teachers);
  // where weigth = teacher.getWeigth()
  List<Teacher> result = mOrm.queryEntityList(teacher, Teacher.class.getField("weigth"));
  mOrm.deleteEntity(teachers.get(0), Teacher.class.getField("weigth"));
  mOrm.updateEntity(teacher, Teacher.class.getField("age"));
  ```
