package com.echoeyecodes.sinnerman.Interface;

import com.echoeyecodes.sinnerman.Paging.CommonListPagingListeners;

public interface CommentActivityListener extends CommonListPagingListeners {

    void onCommentInserted();
    void retry();
}
