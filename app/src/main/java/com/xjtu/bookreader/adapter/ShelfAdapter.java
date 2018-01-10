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
import com.xjtu.bookreader.bean.ShelfBookItemBean;
import com.xjtu.bookreader.databinding.ShelfFooterItemBookBinding;
import com.xjtu.bookreader.databinding.ShelfHeaderItemBookBinding;
import com.xjtu.bookreader.databinding.ShelfItemBookBinding;
import com.xjtu.bookreader.db.BookDBHelper;
import com.xjtu.bookreader.ui.MainActivity;
import com.xjtu.bookreader.util.Logger;
import com.xjtu.bookreader.util.PerfectClickListener;
import com.xjtu.bookreader.util.StringResourceUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 书籍Adapter，用户点击后跳转到书籍阅读页面
 * Created by jingbin on 2016/12/15.
 *
 */

public class ShelfAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private MainActivity context;

    private BookDBHelper bookDBHelper;

    private int status = 1;
    public static final int LOAD_MORE = 0;
    public static final int LOAD_PULL_TO = 1;
    public static final int LOAD_NONE = 2;
    private static final int LOAD_END = 3;
    private static final int TYPE_TOP = -1;

    private static final int TYPE_FOOTER_BOOK = -2;
    private static final int TYPE_HEADER_BOOK = -3;
    private static final int TYPE_CONTENT_BOOK = -4;
    private List<ShelfBookItemBean> list;

    private BookCollectionShadow myCollection;

    public ShelfAdapter(Context context) {
        this.context = (MainActivity) context;
        list = new ArrayList<>();

        initData(context);
    }

    /**
     * 测试时，初始化数据库
     */
    private void initData(Context context) {
        final String path = Environment.getExternalStorageDirectory() + "/Download";
        Logger.d("----------------------------------------------> " +path);
        bookDBHelper = new BookDBHelper(context);
        bookDBHelper.insertBook("1", path  + "/芳华.epub");
        bookDBHelper.insertBook("2", path + "/步履不停.epub");
        bookDBHelper.insertBook("3", path + "/艺术的故事.epub");
        bookDBHelper.insertBook("4", path + "/我的前半生.epub");
        bookDBHelper.insertBook("5", path + "/百年孤独.epub");
        bookDBHelper.insertBook("6", path + "/活着.epub");
        bookDBHelper.insertBook("7", path + "/人间失格.epub");
        bookDBHelper.insertBook("8", path + "/月亮与六便士.epub");
        int rows = bookDBHelper.numberOfRows();

    }


    /**
     * getItemViewType
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER_BOOK;
        } else if (position + 1 == getItemCount()) {
            return TYPE_FOOTER_BOOK;
        } else {
            return TYPE_CONTENT_BOOK;
        }
    }

    /**
     * onCreateViewHolder
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
            case TYPE_FOOTER_BOOK:
                ShelfFooterItemBookBinding mBindFooter = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.shelf_footer_item_book, parent, false);
                return new FooterViewHolder(mBindFooter.getRoot());
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
        } else if (holder instanceof FooterViewHolder) {
            FooterViewHolder footerViewHolder = (FooterViewHolder) holder;
            footerViewHolder.bindItem();
        } else if (holder instanceof BookViewHolder) {
            BookViewHolder bookViewHolder = (BookViewHolder) holder;
            if (list != null && list.size() > 0) {
                // 内容从"1"开始
//                DebugUtil.error("------position: "+position);
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
        return list.size() + 2;
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
                    return (isHeader(position) || isFooter(position)) ? gridManager.getSpanCount() : 1;
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
        if (lp != null
                && lp instanceof StaggeredGridLayoutManager.LayoutParams
                && (isHeader(holder.getLayoutPosition()) || isFooter(holder.getLayoutPosition()))) {
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
            p.setFullSpan(true);
        }
    }

    /**
     * 这里规定 position = 0 时
     * 就为头布局，设置为占满整屏幕宽
     */
    private boolean isHeader(int position) {
        return position >= 0 && position < 1;
    }

    /**
     * 这里规定 position =  getItemCount() - 1时
     * 就为尾布局，设置为占满整屏幕宽
     * getItemCount() 改了 ，这里就不用改
     */
    private boolean isFooter(int position) {
        return position < getItemCount() && position >= getItemCount() - 1;
    }

    public void setMyCollection(BookCollectionShadow myCollection) {
        this.myCollection = myCollection;
    }


    /**
     * footer view
     */
    private class FooterViewHolder extends RecyclerView.ViewHolder {

        ShelfFooterItemBookBinding mBindFooter;

        FooterViewHolder(View itemView) {
            super(itemView);
            mBindFooter = DataBindingUtil.getBinding(itemView);
            mBindFooter.rlMore.setGravity(Gravity.CENTER);
//            LinearLayoutCompat.LayoutParams params = new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ScreenUtils.dipToPx(context, 40));
//            itemView.setLayoutParams(params);
        }

        private void bindItem() {
            switch (status) {
                case LOAD_MORE:
                    mBindFooter.progress.setVisibility(View.VISIBLE);
                    mBindFooter.tvLoadPrompt.setText(StringResourceUtil.getStringById(R.string.loading_more));
                    itemView.setVisibility(View.VISIBLE);
                    break;
                case LOAD_PULL_TO:
                    mBindFooter.progress.setVisibility(View.GONE);
                    mBindFooter.tvLoadPrompt.setText(context.getString(R.string.loading_more));
                    itemView.setVisibility(View.VISIBLE);
                    break;
                case LOAD_NONE:
                    mBindFooter.progress.setVisibility(View.GONE);
                    mBindFooter.tvLoadPrompt.setText(context.getString(R.string.nomore_loading));
                    break;
                case LOAD_END:
                    itemView.setVisibility(View.GONE);
                default:
                    break;
            }
        }
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

        ShelfItemBookBinding mBindBook;

        BookViewHolder(View view) {
            super(view);
            mBindBook = DataBindingUtil.getBinding(view);
        }

        private void bindItem(final ShelfBookItemBean shelfBookItemBean, int position) {
            mBindBook.setBean(shelfBookItemBean);
            mBindBook.executePendingBindings();

            mBindBook.llItemTop.setOnClickListener(new PerfectClickListener() {
                @Override
                protected void onNoDoubleClick(View v) {
                    // 这里要跳转到阅读页面
//                    ReaderObject folioReaderObject = new ReaderObject();
//                    folioReaderObject.setId(shelfBookItemBean.getId());
//                    folioReaderObject.setTitle(shelfBookItemBean.getTitle());

                    // 查询数据库获取
                    Cursor cursor = bookDBHelper.getBook(shelfBookItemBean.getId());
                    cursor.moveToFirst();
                    int index = cursor.getColumnIndex(BookDBHelper.BOOK_COLUMN_BOOK_PATH);
                    final String bookPath = cursor.getString(index);

//                    final String bookPath = Environment.getExternalStorageDirectory() + "/Download" + "/芳华.epub";
                    if (myCollection != null) {
                        myCollection.bindToService(context, new Runnable() {
                            public void run() {
                                Book book = myCollection.getBookByFile(bookPath);
                                if (book != null) {
                                    openBook(book);
                                } else {
                                    Toast.makeText(context, "打开失败,请重试", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }


//                    String highlightPath = cursor.getString(cursor.getColumnIndex(BookDBHelper.BOOK_COLUMN_HIGHLIGHT_PATH));
//                    folioReaderObject.setBookPath(bookPath);
//                    folioReaderObject.setHighlightPath(highlightPath);
//                    ReadActivity.start(context, folioReaderObject);

                }
            });

        }
    }

    /**
     * 跳转到阅读Activity
     * @param data
     */
    private void openBook(Book data) {
        KooReader.openBookActivity(context, data, null);
        context.overridePendingTransition(com.ninestars.android.R.anim.tran_fade_in, com.ninestars.android.R.anim.tran_fade_out);
    }


    public void updateLoadStatus(int status) {
        this.status = status;
        notifyDataSetChanged();
    }

    public int getLoadStatus() {
        return this.status;
    }


    public List<ShelfBookItemBean> getList() {
        return list;
    }

    public void setList(List<ShelfBookItemBean> list) {
        this.list.clear();
        this.list = list;
    }

    public void addAll(List<ShelfBookItemBean> list) {
        this.list.addAll(list);
    }





}
