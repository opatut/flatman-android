package de.opatut.flatman;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import de.opatut.flatman.data.DataStorage;
import de.opatut.flatman.data.DataStorage.ReplyStatus;

public class ShoppingItemActivity extends Activity implements OnClickListener {
	public static Pattern NUMBER_PATTERN = Pattern.compile("-?[\\d,.]+");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shopping_item);

		((ImageButton) findViewById(R.id.button_more)).setOnClickListener(this);
		((ImageButton) findViewById(R.id.button_less)).setOnClickListener(this);
		((Button) findViewById(R.id.button_cancel)).setOnClickListener(this);
		((Button) findViewById(R.id.button_next)).setOnClickListener(this);
		((Button) findViewById(R.id.button_save)).setOnClickListener(this);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.shopping_item_add, menu);
		return super.onCreateOptionsMenu(menu);
	}

	static double getStepSize(double amount, boolean up) {
		if (amount == 0)
			return 1;
		double a = Math.pow(10, Math.floor(Math.log10(amount)));
		if (a == amount && !up) {
			return a / 10;
		} else {
			return a;
		}
	}

	static double getAmountNumber(String amount) {
		Matcher m = NUMBER_PATTERN.matcher(amount);
		if (m.find()) {
			String num = m.group();
			num.replace(',', '.');
			return Double.parseDouble(num);
		}
		return amount.equals("") ? 0 : 1;
	}

	static String setAmountNumber(String amount, double number) {
		Matcher m = NUMBER_PATTERN.matcher(amount);
		if (m.find()) {
			return m.replaceFirst(formatDouble(number));
		} else if (amount.equals("") || amount == null) {
			return formatDouble(number);
		} else {
			return formatDouble(number) + " " + amount;
		}
	}

	@SuppressLint("DefaultLocale")
	static String formatDouble(double f) {
		if (f == (int) f) {
			return String.format("%d", (int) f);
		} else {
			return String.format("%s", f);
		}
	}

	String getAmount() {
		return ((EditText) findViewById(R.id.amount)).getText().toString();
	}

	void setAmount(String amount) {
		((EditText) findViewById(R.id.amount)).setText(amount);
	}

	void increaseAmount() {
		double a = getAmountNumber(getAmount());
		double step = getStepSize(a, true);
		a += step;
		a = Math.floor(a / step) * step;
		setAmount(setAmountNumber(getAmount(), a));
	}

	void decreaseAmount() {
		double a = getAmountNumber(getAmount());
		double step = getStepSize(a, false);
		a -= step;
		a = Math.ceil(a / step) * step;
		setAmount(setAmountNumber(getAmount(), a));
	}

	private boolean addShoppingItem() {
		String title = ((EditText) findViewById(R.id.title)).getText().toString();
		String amount = getAmount();
		String description = ((EditText) findViewById(R.id.description)).getText().toString();

		if (title.length() == 0) {
			return false;
		}

		final Map<String, String> data = new HashMap<String, String>();
		data.put("title", title);
		data.put("amount", amount);
		data.put("description", description);

		new Thread(new Runnable() {
			public void run() {
				DataStorage.getInstance().load("/shopping/item/new", data, ReplyStatus.class, null);
				DataStorage.getInstance().reload();
			}
		}).start();

		Toast.makeText(getApplicationContext(), "Item added", Toast.LENGTH_LONG).show();
		((Button) findViewById(R.id.button_cancel)).setText(R.string.done);
		
		// TODO
		return true;
	}

	private void clear() {
		((EditText) findViewById(R.id.title)).setText("");
		((EditText) findViewById(R.id.amount)).setText("");
		((EditText) findViewById(R.id.description)).setText("");

		((EditText) findViewById(R.id.title)).requestFocus();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_cancel:
			finish();
			break;
		case R.id.button_save:
			addShoppingItem();
			finish();
			break;
		case R.id.button_next:
			addShoppingItem();
			clear();
			break;
		case R.id.button_more:
			increaseAmount();
			break;
		case R.id.button_less:
			decreaseAmount();
			break;
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == R.id.action_next) {
			addShoppingItem();
			clear();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
