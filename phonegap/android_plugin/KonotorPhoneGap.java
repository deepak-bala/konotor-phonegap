/*
Copyright (c) 2013 Demach Software India Private Limited
*/

package com.demach.plugin;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.demach.konotor.*;


public class KonotorPhoneGap extends CordovaPlugin {

  private static final String INIT="init";
  private static final String LAUNCH_FEEDBACK="launchFeedbackScreen";

  private static final String SET_IDENTIFIER="setIdentifier";
  private static final String SET_USERNAME="setUserName";
  private static final String SET_USEREMAIL="setUserEmail";
  private static final String SET_USERMETA="setUserMeta";
  private static final String SET_WELCOMEMESSAGE="setWelcomeMessage";
  private static final String UPDATE="update";
  private static final String GET_UNREADCOUNT="getUnreadCount";
  private static final String REGISTER_UNREADCOUNTCHANGE="registerUnreadCountChangedCallback";
 
  public static final String KONOTOR_LOCAL_BROADCAST_UNREADMESSAGECOUNT = "Konotor_UnreadMessageCountChanged";

 // private CallbackContext notificationCallback;

  @Override
  public boolean execute (String function, JSONArray args,
                          CallbackContext callbackContext) {
	  
	  try{
		   if (INIT.equals(function)) {
		          final String appID = args.getString(0);
		          final String appKey=args.getString(1);
		          cordova.getThreadPool().execute(new Runnable() {
		              public void run() {
		                  Konotor.getInstance(cordova.getActivity().getApplicationContext())
		                  .init(appID,
		                        appKey);
		             //     callbackContext.success();
		              }
		          });
		          
		          return true;

		      }
		      
		      else if (LAUNCH_FEEDBACK.equals(function)) {
		          Konotor.getInstance(cordova.getActivity().getApplicationContext())
		          .launchFeedbackScreen(cordova.getActivity());
		          callbackContext.success();
		          return true;
		      }
		      
		      else if (SET_USERMETA.equals(function)) {
		          final String metaField = args.getString(0);
		          final String metaValue=args.getString(1);
		    //      cordova.getThreadPool().execute(new Runnable() {
//		              public void run() {
		                  Konotor.getInstance(cordova.getActivity().getApplicationContext()).withUserMeta(metaField,metaValue).update();
		                //  callbackContext.success();
//		              }
		      //    });
		          
		          return true;
		          
		      }
		      
		      else if (SET_WELCOMEMESSAGE.equals(function)) {
		          final String welcomeString = args.getString(0);
		     //     cordova.getThreadPool().execute(new Runnable() {
		     //         public void run() {
		                  Konotor.getInstance(cordova.getActivity().getApplicationContext()).withWelcomeMessage(welcomeString);
		             //     callbackContext.success();
		    //          }
		    //      });
		          
		          return true;
		          
		      }
		      
		      else if (SET_IDENTIFIER.equals(function)) {
		          
		          final String identifier = args.getString(0);
		    //      cordova.getThreadPool().execute(new Runnable() {
		     //         public void run() {
		                  Konotor.getInstance(cordova.getActivity().getApplicationContext()).withIdentifier(identifier).update();
		               //   callbackContext.success();
		       //       }
		       //   });
		          
		          return true;

		          
		      }
		      
		      else if (SET_USEREMAIL.equals(function)) {
		          
		          final String email = args.getString(0);
		          cordova.getThreadPool().execute(new Runnable() {
		              public void run() {
		                  Konotor.getInstance(cordova.getActivity().getApplicationContext()).withUserEmail(email).update();
		            //      callbackContext.success();
		              }
		          });
		          
		          return true;
		          
		      }
		      
		      else if (SET_USERNAME.equals(function)) {
		          
		          final String username = args.getString(0);
		          cordova.getThreadPool().execute(new Runnable() {
		              public void run() {
		                  Konotor.getInstance(cordova.getActivity().getApplicationContext()).withUserName(username).update();
		           //       callbackContext.success();
		              }
		          });
		          
		          return true;
		          
		      }
              else if (UPDATE.equals(function)) {
		         cordova.getThreadPool().execute(new Runnable() {
		              public void run() {
		                  Konotor.getInstance(cordova.getActivity().getApplicationContext()).update();
                          //       callbackContext.success();
		              }
		          });
		          
		          return true;
		          
		      }

              else if (REGISTER_UNREADCOUNTCHANGE.equals(function)) {
                  //final String callbackfunction = args.getString(0);

                  BroadcastReceiver konotor_broadcastReceiver = new BroadcastReceiver()
                  {
                      @Override
                      public void onReceive(Context context, Intent intent) {
                       //   Log.d(TAG,"Got Local Intent");
                    	  int unreadcount=Konotor.getInstance(cordova.getActivity().getApplicationContext()).getUnreadMessageCount();
                    	  KonotorPhoneGap.this.webView.sendJavascript("Konotor.setUnreadCount("+unreadcount+")");
                    	  KonotorPhoneGap.this.webView.sendJavascript("Konotor.unreadCountChanged()");
                      }
                  };
                  IntentFilter konotorIntentFilter = new IntentFilter(KONOTOR_LOCAL_BROADCAST_UNREADMESSAGECOUNT);
                  LocalBroadcastManager.getInstance(cordova.getActivity().getApplicationContext()).registerReceiver(konotor_broadcastReceiver, konotorIntentFilter);;
		          
		          return true;
		          
		      }
          
              else if (GET_UNREADCOUNT.equals(function)) {
		          
                  int count=Konotor.getInstance(cordova.getActivity().getApplicationContext()).getUnreadMessageCount();
                  callbackContext.success(count);
		          
		          return true;
		          
		      }

		      else {
		          
		        callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.INVALID_ACTION));
		        return false;
		          
		      }
		  } 
	  catch(Exception e){
	  return false;
	  }
  }
  
}
