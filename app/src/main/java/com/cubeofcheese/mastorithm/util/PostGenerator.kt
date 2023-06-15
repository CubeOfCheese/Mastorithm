package com.cubeofcheese.mastorithm.util

import com.cubeofcheese.mastorithm.models.TestData
import com.cubeofcheese.mastorithm.models.PostModel
import com.keylesspalace.tusky.util.parseAsMastodonHtml

fun generatePost(status: TestData): PostModel {
    var post: PostModel

    var mainStatus : TestData
    var presenceReason : String? = null
    var imagePreview : String? = null

    if (status.reblog != null) {
        presenceReason = "Boosted by " + status.account.display_name
        mainStatus = status.reblog
    } else {
        mainStatus = status

        if (mainStatus.in_reply_to_account_id != null) {
            presenceReason = "In reply to someone" // + mainStatus.in_reply_to_account_id
        }
    }

    if (mainStatus.mediaAttachments.isNotEmpty() && mainStatus.mediaAttachments[0].type == "image") {
        imagePreview = mainStatus.mediaAttachments[0].preview_url
    }

    post = PostModel(
        mainStatus.id,
        mainStatus.account.display_name,
        mainStatus.account.acct,
        mainStatus.account.avatar_static,
        mainStatus.content.parseAsMastodonHtml().toString(),
        imagePreview,
        presenceReason,
        mainStatus.replies_count,
        mainStatus.reblogs_count,
        mainStatus.favourites_count,
    )

    return post
}