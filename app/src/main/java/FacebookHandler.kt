package com.anjay.notify

import android.os.Bundle
import com.facebook.AccessToken
import com.facebook.GraphRequest

class FacebookHandler {
    companion object {
        var accessToken: AccessToken = AccessToken(
            "EAARH1QElcjMBAI63Gd92TF44QJh3JMhCidIwkIPHVKgiXBZB9Pw56pYTBv43tlu0X9jeESOWgoz90vmg5OWkyUl6qvTLU4mLPWsvHQyN8mjTGWrGz5ocgvHOy6T3BGsTphaWdONvgbi95xYp7ZAFkSblRZAGY0KRfSwpq48wimuwUlfqssfnPQmvm9CAExFEq2dEzsJop7maHMRQYB9",
            "1204880079680051",
            "106412340868788",
            null,
            null,
            null,
            null,
            null,
            null,
            null
        )

        fun getPosts() {
            val request = GraphRequest.newMeRequest(
                accessToken
            ) { `object`, response ->
                // Insert your code here
            }

            val parameters = Bundle()
            parameters.putString("fields", "posts{id,message,updated_time,message_tags},id")
            request.parameters = parameters
            lg(request.executeAndWait().rawResponse)
        }
    }
}


