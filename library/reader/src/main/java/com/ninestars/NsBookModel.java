package com.ninestars;

import com.koolearn.klibrary.text.model.ZLTextModel;
import com.koolearn.klibrary.text.model.ZLTextPlainModel;
import com.koolearn.kooreader.bookmodel.BookModel;

public class NsBookModel extends BookModel {

    protected NsBookModel(com.koolearn.kooreader.book.Book book) {
        super(book);
    }

    public ZLTextModel createTextModel(
            String id, String language, int paragraphsNumber,
            int[] entryIndices, int[] entryOffsets,
            int[] paragraphLenghts, int[] textSizes, byte[] paragraphKinds,
            String directoryName, String fileExtension, int blocksNumber
    ) {

        /**
         * 试读默认读取总章节数的20%
         * 最少试读章节不能小于两章，不能大于5章
         */
        int chapterCount = TOCTree.subtrees().size();//章节数量
        int limitCount = (int) (chapterCount * 0.2);//试读章节数
        limitCount = limitCount < 2 ? 2 : limitCount;
        limitCount = limitCount > 5 ? 5 : limitCount;
        limitCount = limitCount > chapterCount - 1 ? chapterCount - 1 : limitCount;

        paragraphsNumber = TOCTree.subtrees().get(limitCount).getReference().ParagraphIndex - 1;



        return new ZLTextPlainModel(
                id, language, paragraphsNumber,
                entryIndices, entryOffsets,
                paragraphLenghts, textSizes, paragraphKinds,
                directoryName, fileExtension, blocksNumber, myImageMap, FontManager
        );
    }
}
