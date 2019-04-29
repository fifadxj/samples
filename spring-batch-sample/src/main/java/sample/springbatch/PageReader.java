package sample.springbatch;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PageReader<T> extends ItemCountingReader<T> {
    private int pageSize = 10;
    private int pageNum = 0;
    private int indexOfPage = 0;
    private List<T> pageData;

    @Override
    protected T doRead() {
        //首次读取
        if (pageData == null) {
            readFromDB(pageNum, indexOfPage);
            pageNum ++;
        }

        //当前页已读完
        if (indexOfPage >= pageSize) {
            readFromDB(pageNum, indexOfPage);
            pageNum ++;
            indexOfPage = 0;
        }

        int next = indexOfPage++;
        if (next < pageData.size()) {
            return pageData.get(next);
        }
        else {
            return null;
        }
    }

    protected void jumpToItem(int itemIndex) {
        pageNum = itemIndex / pageSize;
        indexOfPage = itemIndex % pageSize;
    }

    protected void readFromDB(int pageNum, int pageSize) {
        pageData = new ArrayList<>();
    }
}
