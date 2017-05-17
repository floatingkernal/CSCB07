package sql;

import android.app.Application;
import android.content.Context;

/**
 * Shell application to attach the SQL database to.
 * 
 * @author Cameron
 */
public class SqlApp extends Application {
  private Context appContext;

  @Override
  public void onCreate() {
    super.onCreate();
    appContext = getApplicationContext();
  }

  public Context getContext() {
    return appContext;
  }
}