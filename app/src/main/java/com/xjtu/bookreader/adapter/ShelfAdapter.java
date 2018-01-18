package com.xjtu.bookreader.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.koolearn.android.kooreader.libraryService.BookCollectionShadow;
import com.koolearn.kooreader.book.Book;
import com.koolearn.kooreader.book.Bookmark;
import com.xjtu.bookreader.R;
import com.xjtu.bookreader.bean.model.BookOfShelf;
import com.xjtu.bookreader.databinding.ShelfHeaderItemBookBinding;
import com.xjtu.bookreader.databinding.ShelfItemBookBinding;
import com.xjtu.bookreader.ui.MainActivity;
import com.xjtu.bookreader.util.DebugUtil;
import com.xjtu.bookreader.util.KooreaderUtil;
import com.xjtu.bookreader.util.PerfectClickListener;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

/**
 * 书籍Adapter，用户点击后跳转到书籍阅读页面
 * Created by jingbin on 2016/12/15.
 */

public class ShelfAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private MainActivity activity;

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

    private List<BookOfShelf> deleteList;

    // 是否处于编辑模式
    private boolean isEdit = false;

    public ShelfAdapter(Context activity, BookCollectionShadow myCollection) {
        this.activity = (MainActivity) activity;
        this.myCollection = myCollection;

        list = new ArrayList<>();
        KooreaderUtil.verifyStoragePermissions(this.activity);

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
        } else if (holder instanceof BookViewHolder) {
            BookViewHolder bookViewHolder = (BookViewHolder) holder;
            if (list != null && list.size() > 0) {
                // 内容从"1"开始 bookViewHolder.bindItem(list.get(position - 1), position - 1);
//                DebugUtil.debug("list.size() ----------------> " + list.size());
                bookViewHolder.bindItem(list.get(position - 1), position - 1);
            }
        }
    }

    /**
     * 因为增加了一个头部布局
     *
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

    // 设置是否处于编辑状态
    public void setEdit(boolean isEdit) {
        this.isEdit = isEdit;
        if (isEdit || deleteList == null) {
            deleteList = new ArrayList<>();
        } else {
            deleteList = null;
        }
        notifyDataSetChanged();
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

            DebugUtil.debug("bindItem ---------> isEdit : " + isEdit);
            // 如果是编辑状态
            if (isEdit) {
                mBindBook.ivSelectImg.setVisibility(View.VISIBLE);
                mBindBook.ivSelectImg.setImageResource(R.drawable.delete_select_normal);
            } else { // 不是编辑状态
                mBindBook.ivSelectImg.setVisibility(View.GONE);
            }

            // 判断是否已经下载
            if (bookOfShelf.isDownloaded()) {
                mBindBook.ivDownloadImg.setVisibility(View.GONE);
                mBindBook.pbDownloading.setVisibility(View.GONE);

                mBindBook.ivEditBackground.setVisibility(View.GONE);
            } else {
                mBindBook.ivDownloadImg.setVisibility(View.VISIBLE);
                mBindBook.ivEditBackground.setVisibility(View.VISIBLE);
                mBindBook.ivDownloadImg.setImageResource(R.drawable.download_start);
                mBindBook.pbDownloading.setVisibility(View.GONE); // 点击后才设置可见
            }

            mBindBook.llItemTop.setOnClickListener(new PerfectClickListener() {
                @Override
                protected void onNoDoubleClick(View v) {
                    if (isEdit) { //如果处于编辑状态
                        // 选择就更换右上角图片，并且将当前item加入deleteList
                        if (deleteList.contains(bookOfShelf)) {
                            mBindBook.ivSelectImg.setImageResource(R.drawable.delete_select_normal);
                            deleteList.remove(bookOfShelf);
                        } else {
                            mBindBook.ivSelectImg.setImageResource(R.drawable.delete_select_press);
                            deleteList.add(bookOfShelf);
                        }

                    } else {
                        // 判断书籍是否已经下载
                        if (bookOfShelf.isDownloaded()) {
                            final String bookPath = bookOfShelf.getBookPath();
                            final long bookId = bookOfShelf.getId();

                            if (myCollection != null) {
                                myCollection.bindToService(activity, new Runnable() {
                                    public void run() {
                                        Book book = myCollection.getBookByFile(bookPath);
                                        if (book != null) {
                                            book.setTitle(bookOfShelf.getTitle());
                                            book.setMyCoverPath(bookOfShelf.getCoverImage());
                                            book.setId(bookId);

                                            DebugUtil.debug("bookOfShelf.getId() ------------------> " + bookId);
//                                        book.setBookId(bookOfShelf.getId());
                                            // 这里打开书籍，返回时需要回传时间和进度。
                                            Bookmark bookmark = null;
                                            KooreaderUtil.openBookForResult(activity, MainActivity.REQUEST_FROM_SHELF_ADAPTER, book, bookmark);
                                        } else {
                                            Toasty.error(activity, "打开失败，文件不存在, paht --->" + bookPath).show();
                                        }
                                    }
                                });
                            }
                        } else {
                            // 如果没有下载，点击则下载； 然后更新数据库
                            mBindBook.ivDownloadImg.setImageResource(R.drawable.download_pause);
                            mBindBook.pbDownloading.setVisibility(View.VISIBLE); // 点击后才设置可见
//                        mBindBook.pbDownloading.setProgress();
                            // 开始下载，设置进度
                        }

                    }

                }
            });

        }
    }


//    public void onDeleteEvent(ShelfDeleteEvent event) {
//
//        if (deleteList == null || deleteList.size() == 0)
//            return;
//
//        DebugUtil.debug("deleteList.size() ------------------> " + deleteList.size());
//
//        // 更新数据库
//        for (BookOfShelf item : deleteList) {
//            item.setDeleted(1);
//            item.save();
//        }
//
//        // 移除数据
//        list.removeAll(deleteList);
//
//
//        //退出编辑状态
//        this.isEdit = false;
//        notifyDataSetChanged();
//    }


    public List<BookOfShelf> getDeleteList() {
        return deleteList;
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
