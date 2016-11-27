package com.app.heyphil;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class XmlParser {

    static final String URL = "https://apps.philcare.com.ph/PCareWebServices/CMS.svc/HealthBlogs/?Top=5";
    // XML node keys
    static final String KEY_HealthBlog = "a:HealthBlog"; // parent node
    static final String KEY_Header = "a:Header";
    static final String KEY_Link = "a:Link";

    public ListView listView;
    ChatBubbleActivity chatBubbleActivity = new ChatBubbleActivity();




}
