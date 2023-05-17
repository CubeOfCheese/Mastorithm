package com.cubeofcheese.mastorithm.util

import com.cubeofcheese.mastorithm.TestData
import com.cubeofcheese.mastorithm.models.PostModel
import com.keylesspalace.tusky.util.parseAsMastodonHtml

fun generatePost(status: TestData): PostModel {
    var post: PostModel

    if (status.reblog != null) {
        if (status.reblog.mediaAttachments.isNotEmpty() && status.reblog.mediaAttachments[0].type == "image") {
            post = PostModel(
                status.id,
                status.reblog.account.display_name,
                status.reblog.account.acct,
                status.reblog.account.avatar_static,
                status.reblog.content.parseAsMastodonHtml(),
                status.reblog.mediaAttachments[0].preview_url,
                "Boosted by " + status.account.display_name,
                status.reblog.replies_count,
                status.reblog.reblogs_count,
                status.reblog.favourites_count,
            )
        } else {
            post = PostModel(
                status.id,
                status.reblog.account.display_name,
                status.reblog.account.acct,
                status.reblog.account.avatar_static,
                status.reblog.content.parseAsMastodonHtml(),
                null,
                "Boosted by " + status.account.display_name,
                status.reblog.replies_count,
                status.reblog.reblogs_count,
                status.reblog.favourites_count,
            )
        }
    }
    else {
        if (status.mediaAttachments.isNotEmpty() && status.mediaAttachments[0].type == "image") {
            post = PostModel(
                status.id,
                status.account.display_name,
                status.account.acct,
                status.account.avatar_static,
                status.content.parseAsMastodonHtml(),
                status.mediaAttachments[0].preview_url,
                null,
                status.replies_count,
                status.reblogs_count,
                status.favourites_count,
            )
        } else {
            post = PostModel(
                status.id,
                status.account.display_name,
                status.account.acct,
                status.account.avatar_static,
                status.content.parseAsMastodonHtml(),
                null,
                null,
                status.replies_count,
                status.reblogs_count,
                status.favourites_count,
            )
        }
    }
    return post

}