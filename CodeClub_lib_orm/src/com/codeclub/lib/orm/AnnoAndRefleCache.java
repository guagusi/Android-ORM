package com.codeclub.lib.orm;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.HashMap;

import android.mtp.MtpConstants;
import android.text.TextUtils;
import android.util.Log;

import com.codeclub.lib.orm.annotation.Column;
import com.codeclub.lib.orm.annotation.Table;

/**
 * 
 * @Description: 注解反射缓存
 * @author guagusi123@gmail.com
 * @date 2015年4月12日 下午6:26:25
 */
public class AnnoAndRefleCache {
	
	private static final String TAG = AnnoAndRefleCache.class.getSimpleName();
	
	private WeakReference<HashMap<Field, String>> mColumnNamesCache;
	private WeakReference<HashMap<Field, Class<?>>> mFieldTypesCache;
	private WeakReference<HashMap<Field, String>> mColumnTypesCache;
	
	private HashMap<Field, String> mCNameMap;
	private HashMap<Field, Class<?>> mFTypeMap;
	private HashMap<Field, String> mCTypeMap;
	
	private static AnnoAndRefleCache sInstance;
	private static Object sLock = new Object();
	
	private AnnoAndRefleCache() {
		mCNameMap = new HashMap<Field, String>();
		mFTypeMap = new HashMap<Field, Class<?>>();
		mCTypeMap = new HashMap<Field, String>();
		
		mColumnNamesCache = new WeakReference<HashMap<Field, String>>(mCNameMap);
		mColumnTypesCache = new WeakReference<HashMap<Field, String>>(mCTypeMap);
		mFieldTypesCache = new WeakReference<HashMap<Field, Class<?>>>(mFTypeMap);
	}
	
	public static AnnoAndRefleCache getAnnoAndRefleCache() {
		if(sInstance == null) {
			synchronized(sLock) {
				if(sInstance == null) {
					sInstance = new AnnoAndRefleCache();
				}
			}
		}
		
		return sInstance;
	}
	
	/**
	 * 缓存
	 * @param field
	 * @return
	 */
	private void cacheColumnName2RAM(Field field, String columnName) {
		mCNameMap.put(field, columnName);
	}
	
	/**
	 * 没有则缓存
	 * @param field
	 * @return
	 */
	public String getRAMCacheColumnName(Field field) {
		String columnName = mCNameMap.get(field);
		if(TextUtils.isEmpty(columnName)) {
			if(field != null && field.isAnnotationPresent(Column.class)) {
				columnName = CacheHelper.getColumnName(field);
				
				// cache column name
				cacheColumnName2RAM(field, columnName);
				
				// cache column type
				String columnType = CacheHelper.getColumnType(field);
				cacheColumnType2RAM(field, columnType);
				
				// cache field type
				/*Class<?> clazz = CacheHelper.getFieldType(field);
				cacheFieldType2RAM(field, clazz);*/
			}
		}
		return columnName;
	}
	
	/**
	 * 
	 * @param field
	 */
	private void cacheColumnType2RAM(Field field, String columnType) {
		mCTypeMap.put(field, columnType);
	}
	
	/**
	 * 
	 * @param field
	 * @return
	 */
	public String getRAMCacheColumnType(Field field) {
		String columnType = mCTypeMap.get(field);
		if(TextUtils.isEmpty(columnType)) {
			if(field != null && field.isAnnotationPresent(Column.class)) {
				
				// cache column name
				String columnName = CacheHelper.getColumnName(field);
				cacheColumnName2RAM(field, columnName);
				
				// cache column type
				columnType = CacheHelper.getColumnType(field);
				cacheColumnType2RAM(field, columnType);
				
				// 此注解很耗性能
				// cache field type
				/*Class<?> clazz = CacheHelper.getFieldType(field);
				cacheFieldType2RAM(field, clazz);*/
			}
		}
		return columnType;
	}
	
	/**
	 * 
	 * @param field
	 * @param clazz
	 */
	private void cacheFieldType2RAM(Field field, Class<?> clazz) {
		mFTypeMap.put(field, clazz);
	}
	
	/**
	 * 
	 * @param field
	 * @return
	 */
	public Class<?> getRAMCacheFieldType(Field field) {
		Class<?> clazz = mFTypeMap.get(field);
		if(clazz == null) {
			if(field != null && field.isAnnotationPresent(Column.class)) {
				
				// cache column name
				String columnName = CacheHelper.getColumnName(field);
				cacheColumnName2RAM(field, columnName);
				
				// cache column type
				String columnType = CacheHelper.getColumnType(field);
				cacheColumnType2RAM(field, columnType);
				
				// cache field type
				clazz = CacheHelper.getFieldType(field);
				cacheFieldType2RAM(field, clazz);
			}
		}
		return clazz;
	}
	
	private void clearALlCache() {
		
	}

	/**
	 * 
	 * @return
	 */
	public HashMap<Field, String> getColumnNameMap() {
		if(mColumnNamesCache != null) {
			return mColumnNamesCache.get();
		}
		return null;
	}

	/**
	 * 
	 * @return
	 */
	public HashMap<Field, String> getColumnTypeMap() {
		if(mColumnTypesCache != null) {
			return mColumnTypesCache.get();
		}
		return null;
	}

	/**
	 * 
	 * @return
	 */
	public HashMap<Field, Class<?>> getFieldTypeMap() {
		if(mFieldTypesCache != null) {
			return mFieldTypesCache.get();
		}
		return null;
	}
	
}
