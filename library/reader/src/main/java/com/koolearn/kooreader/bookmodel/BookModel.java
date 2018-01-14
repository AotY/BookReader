/*
 * Copyright (C) 2007-2015 FBReader.ORG Limited <contact@fbreader.org>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301, USA.
 */

package com.koolearn.kooreader.bookmodel;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.koolearn.klibrary.core.fonts.FileInfo;
import com.koolearn.klibrary.core.fonts.FontEntry;
import com.koolearn.klibrary.core.fonts.FontManager;
import com.koolearn.klibrary.core.image.ZLImage;
import com.koolearn.klibrary.text.model.CachedCharStorage;
import com.koolearn.klibrary.text.model.ZLTextModel;
import com.koolearn.klibrary.text.model.ZLTextPlainModel;
import com.koolearn.klibrary.ui.android.library.ZLAndroidApplication;
import com.koolearn.kooreader.book.Book;
import com.koolearn.kooreader.formats.BookReadingException;
import com.koolearn.kooreader.formats.BuiltinFormatPlugin;
import com.koolearn.kooreader.formats.FormatPlugin;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * 默认只能读20%，如若想修改可在BookMode类的createTextModel方法中做修改
 */
public class BookModel {

    public static BookModel createModel(Book book, FormatPlugin plugin) throws BookReadingException {
        if (plugin instanceof BuiltinFormatPlugin) {
            final BookModel model = new BookModel(book);
            ((BuiltinFormatPlugin) plugin).readModel(model); // 进入本地解析 传入图书类,图书
            return model;
        }

        throw new BookReadingException(
                "unknownPluginType", null, new String[]{String.valueOf(plugin)}
        );
    }

    public final Book Book;
    public final TOCTree TOCTree = new TOCTree();
    public final FontManager FontManager = new FontManager();

    protected CachedCharStorage myInternalHyperlinks;
    protected final HashMap<String, ZLImage> myImageMap = new HashMap<String, ZLImage>();
    protected ZLTextModel myBookTextModel;
    protected final HashMap<String, ZLTextModel> myFootnotes = new HashMap<String, ZLTextModel>();


    public static final class Label {
        public final String ModelId;
        public final int ParagraphIndex;

        public Label(String modelId, int paragraphIndex) {
            ModelId = modelId;
            ParagraphIndex = paragraphIndex;
        }
    }

    protected BookModel(Book book) {
        Book = book;
    }

    public interface LabelResolver {
        List<String> getCandidates(String id);
    }

    private LabelResolver myResolver;

    public void setLabelResolver(LabelResolver resolver) {
        myResolver = resolver;
    }

    public Label getLabel(String id) {
        Label label = getLabelInternal(id);
        if (label == null && myResolver != null) {
            for (String candidate : myResolver.getCandidates(id)) {
                label = getLabelInternal(candidate);
                if (label != null) {
                    break;
                }
            }
        }
        return label;
    }

    public void registerFontFamilyList(String[] families) {
        FontManager.index(Arrays.asList(families));
    }

    public void registerFontEntry(String family, FontEntry entry) {
        FontManager.Entries.put(family, entry);
    }

    public void registerFontEntry(String family, FileInfo normal, FileInfo bold, FileInfo italic, FileInfo boldItalic) {
        registerFontEntry(family, new FontEntry(family, normal, bold, italic, boldItalic));
    }

    // 进入之前会调用， createTextModel
    public ZLTextModel createTextModel(
            String id, String language, int paragraphsNumber,
            int[] entryIndices, int[] entryOffsets,
            int[] paragraphLenghts, int[] textSizes, byte[] paragraphKinds,
            String directoryName, String fileExtension, int blocksNumber
    ) {

        //  验证用户登录了吗
//        SharedPreferences sharedPreferences = ZLAndroidApplication.getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
//        String name = sharedPreferences.getString("id", "");

//        if ("".equals(name)) {
//            /**
//             * 试读默认读取总章节数的20%
//             * 最少试读章节不能小于两章，不能大于5章
//             */
//            int chapterCount = TOCTree.subtrees().size();//章节数量
//            int limitCount = (int) (chapterCount * 0.2);//试读章节数
//            limitCount = limitCount < 2 ? 2 : limitCount;
//            limitCount = limitCount > 5 ? 5 : limitCount;
//            limitCount = limitCount > chapterCount - 1 ? chapterCount - 1 : limitCount;
//            /**
//             * 获取试读章节的最后一段，通过获取试读章节下一章的第一段然后减去1，就是试读章节的最后一段，
//             * limitcount是从1开始计数
//             * TocTree集合是从0开始计数的
//             */
//            paragraphsNumber = TOCTree.subtrees().get(limitCount).getReference().ParagraphIndex - 1;
//        }

        int chapterCount = TOCTree.subtrees().size();   //章节数量
        Log.d("BookModel", "chapterCount ----------> " + chapterCount); // chapterCount ----------> 11
        Log.d("BookModel", "paragraphsNumber ----------> " + paragraphsNumber); // paragraphsNumber ----------> 2299
        return new ZLTextPlainModel(
                id, language, paragraphsNumber,
                entryIndices, entryOffsets,
                paragraphLenghts, textSizes, paragraphKinds,
                directoryName, fileExtension, blocksNumber, myImageMap, FontManager
        );
    }

    public void setBookTextModel(ZLTextModel model) {
        myBookTextModel = model;
    }

    public void setFootnoteModel(ZLTextModel model) {
        myFootnotes.put(model.getId(), model);
    }

    public ZLTextModel getTextModel() {
        return myBookTextModel;
    }

    public ZLTextModel getFootnoteModel(String id) {
        return myFootnotes.get(id);
    }

    public void addImage(String id, ZLImage image) {
        myImageMap.put(id, image);
    }

    public void initInternalHyperlinks(String directoryName, String fileExtension, int blocksNumber) {
        myInternalHyperlinks = new CachedCharStorage(directoryName, fileExtension, blocksNumber);
    }

    private TOCTree myCurrentTree = TOCTree;

    public void addTOCItem(String text, int reference) {
        myCurrentTree = new TOCTree(myCurrentTree);
        myCurrentTree.setText(text);
        myCurrentTree.setReference(myBookTextModel, reference);
    }

    public void leaveTOCItem() {
        myCurrentTree = myCurrentTree.Parent;
        if (myCurrentTree == null) {
            myCurrentTree = TOCTree;
        }
    }

    private Label getLabelInternal(String id) {
        final int len = id.length();
        final int size = myInternalHyperlinks.size();

        for (int i = 0; i < size; ++i) {
            final char[] block = myInternalHyperlinks.block(i);
            for (int offset = 0; offset < block.length; ) {
                final int labelLength = (int) block[offset++];
                if (labelLength == 0) {
                    break;
                }
                final int idLength = (int) block[offset + labelLength];
                if (labelLength != len || !id.equals(new String(block, offset, labelLength))) {
                    offset += labelLength + idLength + 3;
                    continue;
                }
                offset += labelLength + 1;
                final String modelId = (idLength > 0) ? new String(block, offset, idLength) : null;
                offset += idLength;
                final int paragraphNumber = (int) block[offset] + (((int) block[offset + 1]) << 16);
                return new Label(modelId, paragraphNumber);
            }
        }
        return null;
    }
}