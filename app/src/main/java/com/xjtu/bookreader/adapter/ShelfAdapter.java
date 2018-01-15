package com.xjtu.bookreader.adapter;

import android.content.Context;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.os.Environment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.koolearn.android.kooreader.KooReader;
import com.koolearn.android.kooreader.libraryService.BookCollectionShadow;
import com.koolearn.kooreader.book.Book;
import com.xjtu.bookreader.R;
import com.xjtu.bookreader.bean.model.BookOfShelf;
import com.xjtu.bookreader.databinding.ShelfFooterItemBookBinding;
import com.xjtu.bookreader.databinding.ShelfHeaderItemBookBinding;
import com.xjtu.bookreader.databinding.ShelfItemBookBinding;
import com.xjtu.bookreader.db.BookDBHelper;
import com.xjtu.bookreader.ui.MainActivity;
import com.xjtu.bookreader.util.DebugUtil;
import com.xjtu.bookreader.util.KooreaderUtil;
import com.xjtu.bookreader.util.Logger;
import com.xjtu.bookreader.util.PerfectClickListener;
import com.xjtu.bookreader.util.StringResourceUtil;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

/**
 * 书籍Adapter，用户点击后跳转到书籍阅读页面
 * Created by jingbin on 2016/12/15.
 */

public class ShelfAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private MainActivity context;

    private int status = 1;

    public static final int LOAD_MORE = 0;
    public static final int LOAD_PULL_TO = 1;
    public static final int LOAD_NONE = 2;
    private static final int LOAD_END = 3;
    private static final int TYPE_TOP = -1;

    private static final int TYPE_FOOTER_BOOK = -2;
    private static final int TYPE_HEADER_BOOK = -3;
    private static final int TYPE_CONTENT_BOOK = -4;


    private List<BookOfShelf> list;

    private BookCollectionShadow myCollection;

    public ShelfAdapter(Context context, BookCollectionShadow myCollection) {
        this.context = (MainActivity) context;
        this.myCollection = myCollection;

        list = new ArrayList<>();
        KooreaderUtil.verifyStoragePermissions(this.context);
    }

    /**
     * getItemViewType
     */
    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER_BOOK;
        } else {
            return TYPE_CONTENT_BOOK;
        }
    }

    /**
     * onCreateViewHolder
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_HEADER_BOOK:
                ShelfHeaderItemBookBinding mBindHeader = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.shelf_header_item_book, parent, false);
                return new HeaderViewHolder(mBindHeader.getRoot());
//            case TYPE_FOOTER_BOOK:
//                ShelfFooterItemBookBinding mBindFooter = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.shelf_footer_item_book, parent, false);
//                return new FooterViewHolder(mBindFooter.getRoot());
            default:
                ShelfItemBookBinding mBindBook = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.shelf_item_book, parent, false);
                return new BookViewHolder(mBindBook.getRoot());
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderViewHolder) {
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
            headerViewHolder.bindItem();
        }
//        else if (holder instanceof FooterViewHolder) {
//            FooterViewHolder footerViewHolder = (FooterViewHolder) holder;
//            footerViewHolder.bindItem();
//        }
        else if (holder instanceof BookViewHolder) {
            BookViewHolder bookViewHolder = (BookViewHolder) holder;
            if (list != null && list.size() > 0) {
                // 内容从"1"开始 bookViewHolder.bindItem(list.get(position - 1), position - 1);
                DebugUtil.debug("list.size() ----------------> " +list.size());
                bookViewHolder.bindItem(list.get(position - 1), position - 1);
            }
        }
    }

    /**
     * 这里是？ 一个头，一个尾巴吗？
     * @return
     */
    @Override
    public int getItemCount() {
//        return list.size() + 2;
        return list.size() + 1;
    }


    /**
     * 处理 GridLayoutManager 添加头尾布局占满屏幕宽的情况
     */
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();

        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);

            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
//                    return (isHeader(position) || isFooter(position)) ? gridManager.getSpanCount() : 1;
                    return isHeader(position) ? gridManager.getSpanCount() : 1;
                }
            });
        }
    }


    /**
     * 处理 StaggeredGridLayoutManager 添加头尾布局占满屏幕宽的情况
     */
    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams
                && isHeader(holder.getLayoutPosition())) {
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
            p.setFullSpan(true);
        }
    }

    /**
     * 这里规定 position = 0 时
     * 就为头布局，设置为占满整屏幕宽
     */
    private boolean isHeader(int position) {
//        return position >= 0 && position < 1;
        return position == 0;
    }

    /**
     * HeaderView
     */
    private class HeaderViewHolder extends RecyclerView.ViewHolder {
        ShelfHeaderItemBookBinding mBindBook;
        HeaderViewHolder(View view) {
            super(view);
            mBindBook = DataBindingUtil.getBinding(view);
        }
        private void bindItem() {
        }
    }


    /**
     * BookView
     */
    private class BookViewHolder extends RecyclerView.ViewHolder {

        // 布局文件对应的data binding
        ShelfItemBookBinding mBindBook;

        BookViewHolder(View view) {
            super(view);
            mBindBook = DataBindingUtil.getBinding(view);
        }

        private void bindItem(final BookOfShelf bookOfShelf, int position) {
            mBindBook.setBookOfShelf(bookOfShelf);

            // Evaluates the pending bindings, updating any Views that have expressions bound to modified variables.
            mBindBook.executePendingBindings();

            mBindBook.llItemTop.setOnClickListener(new PerfectClickListener() {
                @Override
                protected void onNoDoubleClick(View v) {
                    // 判断书籍是否已经下载
                    if (bookOfShelf.isDownloaded()) {
                        final String bookPath = bookOfShelf.getBookPath();

                        if (myCollection != null) {
                            myCollection.bindToService(context, new Runnable() {
                                public void run() {
                                    Book book = myCollection.getBookByFile(bookPath);
                                    if (book != null) {
                                        KooreaderUtil.openBook(context, book);
                                    } else {
                                        Toasty.error(context, "打开失败，文件不存在, paht --->" + bookPath).show();
                                    }
                                }
                            });
                        }
                    } else {
                        // 如果没有下载，点击则下载； 然后更新数据库

                    }
                }
            });

        }
    }


    public void updateLoadStatus(int status) {
        this.status = status;
        notifyDataSetChanged();
    }

    public int getLoadStatus() {
        return this.status;
    }


    public List<BookOfShelf> getList() {
        return list;
    }

    public void setList(List<BookOfShelf> list) {
        this.list.clear();
        this.list = list;
    }

    public void addAll(List<BookOfShelf> list) {
        this.list.addAll(list);
    }


}
