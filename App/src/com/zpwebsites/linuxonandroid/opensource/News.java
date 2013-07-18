/*
* Copyright (C) 2013 linuxonandroid.org
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package com.zpwebsites.linuxonandroid.opensource;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class News extends ListBaseActivity {
	
	public News() {
		super(R.string.frontpage_btn_news);
	}

	private RSSFeed myRssFeed = null;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.news);
		setSlidingActionBarEnabled(false);

		new MyTask().execute();
			
	}
	private class MyTask extends AsyncTask<Void, Void, Void>{

		@Override
		protected Void doInBackground(Void... arg0) {
			try {
				URL rssUrl = new URL("http://linuxonandroid.org/feed/");
				SAXParserFactory mySAXParserFactory = SAXParserFactory.newInstance();
				SAXParser mySAXParser = mySAXParserFactory.newSAXParser();
				XMLReader myXMLReader = mySAXParser.getXMLReader();
				RSSHandler myRSSHandler = new RSSHandler();
				myXMLReader.setContentHandler(myRSSHandler);
				InputSource myInputSource = new InputSource(rssUrl.openStream());
				myXMLReader.parse(myInputSource);
				
				myRssFeed = myRSSHandler.getFeed();	
			} catch (MalformedURLException e) {
				e.printStackTrace();	
			} catch (ParserConfigurationException e) {
				e.printStackTrace();	
			} catch (SAXException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();	
			}
			
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if (myRssFeed!=null)
			{
				TextView feedTitle = (TextView)findViewById(R.id.feedtitle);
				TextView feedDescribtion = (TextView)findViewById(R.id.feeddescribtion);
				//TextView feedPubdate = (TextView)findViewById(R.id.feedpubdate);
				//TextView feedLink = (TextView)findViewById(R.id.feedlink);
				feedTitle.setText(myRssFeed.getTitle());
				feedDescribtion.setText(myRssFeed.getDescription());
				//feedPubdate.setText(myRssFeed.getPubdate());
				//feedLink.setText(myRssFeed.getLink());
				
				ArrayAdapter<RSSItem> adapter =
						new ArrayAdapter<RSSItem>(getApplicationContext(),
								R.layout.simple_list_item,myRssFeed.getList());
				setListAdapter(adapter);	
				
			}else{
				
				TextView textEmpty = (TextView)findViewById(android.R.id.empty);
				textEmpty.setText("No Feed Found!");
			}
			
			super.onPostExecute(result);
		}
		
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		
		Uri feedUri = Uri.parse(myRssFeed.getItem(position).getLink());
		Intent myIntent = new Intent(Intent.ACTION_VIEW, feedUri);
		startActivity(myIntent);
	}
}