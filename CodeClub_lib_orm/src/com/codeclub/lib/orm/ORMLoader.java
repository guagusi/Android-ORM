package com.codeclub.lib.orm;

import java.lang.reflect.Field;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


/**
 * 
 * @Description: 
 * @author guagusi123@gmail.com
 * @date 2015年4月11日 下午1:38:18
 */
public class ORMLoader {

	private static final String TAG = ORMLoader.class.getSimpleName();
	
	private static ORMLoader sInstance;
	private static Object sLock = new Object();
	
	private Context mContext;
	
	private String mDBName;
	private int mDBVersion;
	private String[] mDBTables;
	private String[] mTablesSQL;
	
	private ORMHelper mORMHelper;
	
	private ORMLoader(final Context context) {
		mContext = context;
		
		// 读取数据库配置信息
		try {
			mDBName = mContext.getResources().getString(R.string.orm_db_name);
			mDBVersion = Integer.valueOf(mContext.getResources().getString(R.string.orm_db_version));
			mDBTables = mContext.getResources().getStringArray(R.array.tables);
		} catch(Exception e) {
			String errorStr = "orm.xml 配置文件不存在或定义错误";
			Log.e(TAG, errorStr);
			throw new RuntimeException(errorStr);
		}
		
		mTablesSQL = SQLUtils.createTablesSQL(mDBTables);
		
		mORMHelper = new ORMHelper(mContext, mDBName, null, mDBVersion); 
		mORMHelper.getReadableDatabase();
	}
	
	public static final ORMLoader getORMInstance(final Context context) {
		if(sInstance == null) {
			synchronized(sLock) {
				if(sInstance == null) {
					sInstance = new ORMLoader(context);
				}
			}
		}
		return sInstance;
	}
	
	/**
	 * 
	 * @param entity
	 */
	public <E> void insertEntity(E entity) {
		mORMHelper.getWritableDatabase().insert(SQLUtils.getTableName(entity.getClass()), 
				null, ECConvertUtils.convert2CV(entity));
	}
	
	/**
	 * 事务插入
	 * @param entities
	 */
	public <E> void insertEntityList(List<E> entities) {
		if(entities == null || entities.size() <= 0) {
			return;
		}
		
		SQLiteDatabase db = mORMHelper.getWritableDatabase();
		db.beginTransaction();
		try {
			for(E entity : entities) {
				insertEntity(entity);
			}
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}

	}
	
	/**
	 * 
	 * @param entity
	 * @param fields 需要更新的字段
	 */
	public <E> void updateEntity(E entity, Field... fields) {
		String whereClause = null;
		String[] whereArgs = null;
		if(fields != null && fields.length > 0) {
			whereArgs = new String[fields.length];
			whereClause = SQLUtils.getWhereSQL(entity, fields, whereArgs);
		} 
		
		mORMHelper.getWritableDatabase().update(SQLUtils.getTableName(entity.getClass()), 
				ECConvertUtils.convert2CV(entity), whereClause, whereArgs);
	}
	
	/**
	 * 
	 * @param fields
	 * @return
	 */
	public <E> E queryEntity(E entity, Field... fields) {
		String whereClause = null;
		String[] whereArgs = null;
		if(fields != null && fields.length > 0) {
			whereArgs = new String[fields.length];
			whereClause = SQLUtils.getWhereSQL(entity, fields, whereArgs);
		} 
		try {
			Cursor cursor = mORMHelper.getWritableDatabase().query(
					SQLUtils.getTableName(entity.getClass()),
					null, whereClause, whereArgs, null, null, null);
			return ECConvertUtils.convert2Entity(entity, cursor);
		} catch (Exception e) {
			Log.e(TAG, e.toString());
		}
		return null;
	}
	
	/**
	 * 
	 * @param fields
	 * @return
	 */
	public <E> List<E> queryEntityList(E entity, Field... fields) {
		String whereClause = null;
		String[] whereArgs = null;
		if(fields != null && fields.length > 0) {
			whereArgs = new String[fields.length];
			whereClause = SQLUtils.getWhereSQL(entity, fields, whereArgs);
		} 
		
		Class<E> clazz = (Class<E>) entity.getClass();
		long beginTime = System.currentTimeMillis();
		
		Cursor cursor = mORMHelper.getWritableDatabase().query(
				SQLUtils.getTableName(entity.getClass()),
				null, whereClause, whereArgs, null, null, null);
		long endTime = System.currentTimeMillis();
		Log.e(TAG, endTime - beginTime + "");
		return ECConvertUtils.convert2EntityList(clazz, cursor);
	}
	
	/**
	 * 
	 * @param entity
	 */
	public <E> void deleteEntity(E entity,  Field... fields) {
		String whereClause = null;
		String[] whereArgs = null;
		if(fields != null && fields.length > 0) {
			whereArgs = new String[fields.length];
			whereClause = SQLUtils.getWhereSQL(entity, fields, whereArgs);
		} 
		
		mORMHelper.getWritableDatabase().delete(SQLUtils.getTableName(entity.getClass()),
				whereClause, whereArgs);
	}
	
	/**
	 * 
	 * @param entities
	 */
	public <E> void deleteEntityList(List<E> entities) {
		
	}

	/**
	 * 清空数据表
	 * @param clazz
	 */
	public void clearTable(Class<?> clazz) {
		mORMHelper.getWritableDatabase().execSQL(SQLUtils.clearTableSQL(clazz));
	}
	
	/**
	 * 删除数据表
	 */
	public void dropTable(Class<?> clazz) {
		mORMHelper.getWritableDatabase().execSQL(SQLUtils.dropTableSQL(clazz));
	}
	
	/**
	 * 关闭数据库
	 */
	public void closeORM() {
		mORMHelper.getWritableDatabase().close();
	}
	
	private final class ORMHelper extends SQLiteOpenHelper {

		public ORMHelper(Context context, String name, CursorFactory factory,
				int version) {
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			for(String sql : mTablesSQL) {
				db.execSQL(sql);
				Log.i(TAG, sql);
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			for(String sql : mTablesSQL) {
				db.execSQL(sql);
				Log.i(TAG, sql);
			}
			Log.i(TAG, "upgrade db " + mDBName + " from version " + oldVersion + " to version " + newVersion);
		}
		
	}
}
