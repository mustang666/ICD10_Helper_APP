package com.example.icd10_app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.icd10_app.fragmento.Estatisticas;
import com.example.icd10_app.fragmento.Favoritos;
import com.example.icd10_app.fragmento.Home;
import com.example.icd10_app.fragmento.Pesquisar;
import com.example.icd10_app.modelo.Capitulo;
import com.example.icd10_app.modelo.ICD10;

public class MainActivity extends Activity implements ActionBar.TabListener {

	private static final String ICDCAP = "icd102010enMetachapters.txt";

	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	private Pesquisar pesquisar;

	private ListView list;
	private Adapter1 adaptador;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final int[] ICONS = new int[] { R.drawable.home, R.drawable.search,
				R.drawable.favoritos, R.drawable.statistics, };

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.

			actionBar.addTab(actionBar.newTab().setIcon(ICONS[i])
					.setTabListener(this));
		}

		// get an instance of FragmentTransaction from your Activity
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();

		// add a fragment
		pesquisar = new Pesquisar();
		fragmentTransaction.add(pesquisar, "pesquisar");
		fragmentTransaction.commit();

	}

	public void atualizarCapitulos() {

	}

	public boolean isExternalStorageWritable() {
		return Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState());
	}

	private void lerExternamente() {
		if (isExternalStorageWritable()) {
			File file = new File(Environment.getExternalStorageDirectory(),
					ICDCAP);
			FileReader fr = null;
			BufferedReader br = null;

			String[] tokens = null;
			try {

				fr = new FileReader(file);
				br = new BufferedReader(fr);
				String linha;
				while ((linha = br.readLine()) != null) {
					tokens = linha.split(";");

					ICD10.getInstance().adicionarCapitulo(
							new Capitulo(tokens[1], tokens[0]));
				}

			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (br != null)
						br.close();
					if (fr != null)
						fr.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			list = (ListView) findViewById(R.id.listView1);

			adaptador = new Adapter1(getApplicationContext(),
					android.R.layout.simple_list_item_1, ICD10.getInstance()
							.getAll());
			list.setAdapter(adaptador);

		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

		case R.id.persistencia:
			lerExternamente();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {

		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {

			switch (position) {
			case 0:
				return Home.newInstance(position);

			case 1:
				return Pesquisar.newInstance(position);
			case 2:
				return Favoritos.newInstance(position);
			case 3:
				return Estatisticas.newInstance(position);

			}
			return null;

		}

		@Override
		public int getCount() {
			return 4;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			case 2:
				return getString(R.string.title_section3).toUpperCase(l);
			}
			return null;
		}
	}

	public class Adapter1 extends ArrayAdapter<Capitulo> {

		public Adapter1(Context context, int resID, ArrayList<Capitulo> items) {
			super(context, resID, items);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = super.getView(position, convertView, parent);

			((TextView) v).setTextColor(Color.BLACK);
			((TextView) v).setBackgroundColor(Color.LTGRAY);
			list.setDivider(new ColorDrawable(0x99F10529));
			list.setDividerHeight(1);
			return v;
		}

	}
}
