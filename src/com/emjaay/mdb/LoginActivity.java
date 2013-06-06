package com.emjaay.mdb;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.emjaay.mdb.communication.AbstractAsyncTask;
import com.emjaay.mdb.communication.AbstractAsyncTask.CommunicationCallback;
import com.emjaay.mdb.communication.InsertCopyTask;
import com.emjaay.mdb.communication.LoginTask;
import com.emjaay.mdb.communication.RegisterTask;
import com.emjaay.mdb.communication.result.ApiResult;
import com.emjaay.mdb.data.Copy;
import com.emjaay.mdb.database.DatabaseHelper;
import com.emjaay.mdb.util.UserData;
import com.emjaay.mdb.util.Util;

public class LoginActivity extends AbstractFragmentActivity implements CommunicationCallback {

	private static final int MIN_PASSWORD_LENGTH = 4;
	
	private LoginTask loginTask = null;
	private RegisterTask registerTask = null;

	// Values for email and password at the time of the login attempt.
	private String mEmail;
	private String mPassword;

	// UI references.
	private EditText mEmailView;
	private EditText mPasswordView;
	private EditText mRegisterPasswordView1;
	private EditText mRegisterPasswordView2;
	
	private AlertDialog alertRegister;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity);
		
		if(!Util.isNetworkAvailable(this)){
			Intent intent = new Intent(this, HomeActivity.class);
			startActivity(intent);
			finish();
		}

		// Set up the login form.
		mEmail = UserData.getEmail(this);
		mEmailView = (EditText) findViewById(R.id.email);
		mEmailView.setText(mEmail);

		mPasswordView = (EditText) findViewById(R.id.password);
		mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
				if (id == R.id.login || id == EditorInfo.IME_NULL || id == EditorInfo.IME_ACTION_DONE) {
					attemptLogin();
					return true;
				}
				return false;
			}
		});
		
		if(mEmail.length() > 0){
			mPasswordView.requestFocus();
		}

		findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				attemptLogin();
			}
		});
		
		if(UserData.userDataExists(this)){
			syncUnsyncedCopies();
			loginWithCachedUserData();
		}
	}
	
	private void syncUnsyncedCopies() {
		DatabaseHelper database = new DatabaseHelper(this);
		ArrayList<Copy> copies = database.getUnsyncedCopies();
		
		for(Copy copy : copies){
			InsertCopyTask task = new InsertCopyTask(this, copy, null);
			task.execute((Void) null);
		}
	}
	
	private void loginWithCachedUserData() {
		mEmail = UserData.getEmail(this);
		mPassword = UserData.getPassword(this);
		
		if(TextUtils.isEmpty(mEmail) == false && TextUtils.isEmpty(mPassword) == false){
			showProgress(true);
			loginTask = new LoginTask(this, mEmail, mPassword, this);
			loginTask.execute((Void) null);
		}
	}
	
	private void attemptLogin() {
		if (loginTask != null) {
			return;
		}

		// Reset errors.
		mEmailView.setError(null);
		mPasswordView.setError(null);

		// Store values at the time of the login attempt.
		mEmail = mEmailView.getText().toString().trim();
		mPassword = mPasswordView.getText().toString().trim();

		boolean cancel = false;
		View focusView = null;

		// Check for a valid password.
		if (TextUtils.isEmpty(mPassword)) {
			mPasswordView.setError(getString(R.string.error_field_required));
			focusView = mPasswordView;
			cancel = true;
		} else if (mPassword.length() < MIN_PASSWORD_LENGTH) {
			mPasswordView.setError(getString(R.string.error_invalid_password));
			focusView = mPasswordView;
			cancel = true;
		}

		// Check for a valid email address.
		if (TextUtils.isEmpty(mEmail)) {
			mEmailView.setError(getString(R.string.error_field_required));
			focusView = mEmailView;
			cancel = true;
		} else if (!mEmail.contains("@")) {
			mEmailView.setError(getString(R.string.error_invalid_email));
			focusView = mEmailView;
			cancel = true;
		}

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			showProgress(true);
			loginTask = new LoginTask(this, mEmail, Util.hashPassword(mPassword), this);
			loginTask.execute((Void) null);
		}
	}
	
	/**
	 * @return true if an attempt is sent to the server
	 */
	public void attemptRegister() {
		if (registerTask != null) {
			return;
		}

		// Reset errors.
		mRegisterPasswordView1.setError(null);
		mRegisterPasswordView2.setError(null);

		String password1 = mRegisterPasswordView1.getText().toString().trim();
    	String password2 = mRegisterPasswordView2.getText().toString().trim();

		boolean cancel = false;
		View focusView = null;

		// Check for a valid password.
		if (password1.equals(password2) == false) {
			mRegisterPasswordView2.setError(getString(R.string.error_passwords_missmatch));
			focusView = mRegisterPasswordView2;
			cancel = true;
		}
		
		if (TextUtils.isEmpty(password2)) {
			mRegisterPasswordView2.setError(getString(R.string.error_field_required));
			focusView = mRegisterPasswordView2;
			cancel = true;
		} else if (password2.length() < MIN_PASSWORD_LENGTH) {
			mRegisterPasswordView2.setError(getString(R.string.error_invalid_password));
			focusView = mRegisterPasswordView2;
			cancel = true;
		}

		if (TextUtils.isEmpty(password1)) {
			mRegisterPasswordView1.setError(getString(R.string.error_field_required));
			focusView = mRegisterPasswordView1;
			cancel = true;
		} else if (password1.length() < MIN_PASSWORD_LENGTH) {
			mRegisterPasswordView1.setError(getString(R.string.error_invalid_password));
			focusView = mRegisterPasswordView1;
			cancel = true;
		}

		if (cancel) {
			focusView.requestFocus();
		} else {
			// Store values at the time of the register attempt.
			mEmail = mEmailView.getText().toString().trim();
			mPassword = mRegisterPasswordView1.getText().toString().trim();
			
			// Show a progress spinner, and kick off a background task to
			// perform the user registration attempt.
			showProgress(true);
			registerTask = new RegisterTask(this, mEmail, Util.hashPassword(mPassword), this);
			registerTask.execute((Void) null);
		}
		return;
	}
	
	private void showRegisterDialog() {
		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		View view = getLayoutInflater().inflate(R.layout.dialog_create_user, null);
	    builder.setView(view);

	    TextView textView = (TextView) view.findViewById(R.id.create_user_message);
	    mRegisterPasswordView1 = (EditText) view.findViewById(R.id.password1);
	    mRegisterPasswordView2 = (EditText) view.findViewById(R.id.password2);
	    
	    Spanned message = Html.fromHtml(String.format(getString(R.string.email_doesnt_exists), mEmail));
	    textView.setText(message);
	    mRegisterPasswordView1.setText(mPassword);
	    
	    mRegisterPasswordView2.requestFocus();
	    mRegisterPasswordView2.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
				if (id == R.id.register || id == EditorInfo.IME_NULL || id == EditorInfo.IME_ACTION_DONE) {
					attemptRegister();
					return true;
				}
				return false;
			}
		});
	    
	    builder.setPositiveButton(R.string.register, null);
	    
	    builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int whichButton) {
	            dialog.cancel();
	        }
	    });
	    
	    builder.setCancelable(false);
	    alertRegister = builder.show();
	    
	    alertRegister.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				attemptRegister();
			}
		});
	}

	@Override
	public void taskComplete(AbstractAsyncTask task, ApiResult result) {
		if(task == loginTask){
			loginTask = null;
		} else if(task == registerTask){
			registerTask = null;
		}
		showProgress(false);
		
		if(result.isSuccess()) {
			String email = result.getBundle().getString(task instanceof LoginTask ? LoginTask.EXTRA_EMAIL : RegisterTask.EXTRA_EMAIL);
			String hashedPassword = result.getBundle().getString(task instanceof LoginTask ? LoginTask.EXTRA_PASSWORD : RegisterTask.EXTRA_PASSWORD);
			UserData.setEmail(this, email);
			UserData.setPassword(this, hashedPassword);
			
			Intent intent = new Intent(this, HomeActivity.class);
			startActivity(intent);
			finish();
		} else {
			switch(result.getErrorCode()){
			case ApiResult.ERROR_EMAIL_DONT_EXISTS:
				showRegisterDialog();
				break;
			case ApiResult.ERROR_WRONG_PASSWORD:
				UserData.setPassword(this, "");
				mPasswordView.setError(getString(R.string.error_wrong_password));
				mPasswordView.requestFocus();
				break;
			case ApiResult.ERROR_EMAIL_ALREADY_EXISTS:
				alertRegister.dismiss();
				Toast.makeText(this, R.string.error_email_already_used, Toast.LENGTH_SHORT).show();
				break;
			default:
				Toast.makeText(this, R.string.could_not_communicate, Toast.LENGTH_SHORT).show();
				break;
			}
		}
	}

	@Override
	public void taskCancelled(AbstractAsyncTask task) {
		if(task == loginTask){
			loginTask = null;			
		} else if(task == registerTask){
			registerTask = null;
		}
		showProgress(false);
	}
}