///*
//* Copyright (C) 2016 Pedro Paulo de Amorim
//*
//* Licensed under the Apache License, Version 2.0 (the "License");
//* you may not use this file except in compliance with the License.
//* You may obtain a copy of the License at
//*
//* http://www.apache.org/licenses/LICENSE-2.0
//*
//* Unless required by applicable law or agreed to in writing, software
//* distributed under the License is distributed on an "AS IS" BASIS,
//* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//* See the License for the specific language governing permissions and
//* limitations under the License.
//*/
//package com.xjtu.bookreader.ui;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v7.app.AppCompatActivity;
//import android.util.Log;
//import android.widget.Toast;
//
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.folioreader.model.HighLight;
//import com.folioreader.ui.base.OnSaveHighlight;
//import com.folioreader.util.FolioReader;
//import com.folioreader.util.OnHighlightListener;
//import com.xjtu.bookreader.bean.ReaderObject;
//import com.xjtu.bookreader.bean.HighlightData;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.util.ArrayList;
//import java.util.List;
//
//public class ReadActivity extends AppCompatActivity implements OnHighlightListener {
//
//    private FolioReader folioReader;
//
//    public final static String EXTRA_PARAM = "folio_reader_object";
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        // 获取要打开的文件位置
//        Intent intent = getIntent();
//        Bundle bundle = intent.getExtras();
//        ReaderObject folioReaderObject = null;
//        if (bundle != null) {
//            folioReaderObject = (ReaderObject) bundle.getSerializable(EXTRA_PARAM);
//        }
//
//        folioReader = new FolioReader(this);
//        folioReader.registerHighlightListener(this);
//
//        if (folioReaderObject != null) {
//            folioReader.openBook(folioReaderObject.getBookPath());
//
//            getHighlightsAndSave(folioReaderObject.getHighlightPath());
//        }
//
//        finish();
//    }
//
//    /*
//     * For testing purpose, we are getting dummy highlights from asset. But you can get highlights from your server
//     * On success, you can save highlights to FolioReader DB.
//     */
//    private void getHighlightsAndSave(final String highlightPath) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                ArrayList<HighLight> highlightList = null;
//                ObjectMapper objectMapper = new ObjectMapper();
//                try {
//                    highlightList = objectMapper.readValue(
//                            loadAssetTextAsString(highlightPath),
//                            new TypeReference<List<HighlightData>>() {
//                            });
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//                if (highlightList == null) {
//                    folioReader.saveReceivedHighLights(highlightList, new OnSaveHighlight() {
//                        @Override
//                        public void onFinished() {
//                            //You can do anything on successful saving highlight list
//                            Toast.makeText(ReadActivity.this, "successful saving highlight", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                }
//            }
//        }).start();
//    }
//
//    private String loadAssetTextAsString(String name) {
//        BufferedReader in = null;
//        try {
//            StringBuilder buf = new StringBuilder();
//            InputStream is = getAssets().open(name);
//            in = new BufferedReader(new InputStreamReader(is));
//
//            String str;
//            boolean isFirst = true;
//            while ((str = in.readLine()) != null) {
//                if (isFirst)
//                    isFirst = false;
//                else
//                    buf.append('\n');
//                buf.append(str);
//            }
//            return buf.toString();
//        } catch (IOException e) {
//            Log.e("ReadActivity", "Error opening asset " + name);
//        } finally {
//            if (in != null) {
//                try {
//                    in.close();
//                } catch (IOException e) {
//                    Log.e("ReadActivity", "Error closing asset " + name);
//                }
//            }
//        }
//        return null;
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        folioReader.unregisterHighlightListener();
//    }
//
//    @Override
//    public void onHighlight(HighLight highlight, HighLight.HighLightAction type) {
//        Toast.makeText(this,
//                "highlight id = " + highlight.getUUID() + " type = " + type,
//                Toast.LENGTH_SHORT).show();
//    }
//
//
//    /**
//     * 跳转到该页面，传入文件路径和高亮路径
//     */
//    public static void start(Activity context, ReaderObject folioReaderObject) {
//        Intent intent = new Intent(context, ReadActivity.class);
//
//        Bundle bundle = new Bundle();
//        bundle.putSerializable(EXTRA_PARAM, folioReaderObject);
//        intent.putExtras(bundle);
//        context.startActivity(intent);
//    }
//
//}