package com.anjay.notify

import android.os.Bundle
import com.facebook.AccessToken
import com.facebook.GraphRequest
import com.facebook.GraphResponse
import org.json.JSONObject





class FacebookHandler {
    companion object {
        var accessToken: AccessToken = AccessToken(
            "EAARH1QElcjMBAJkL47TmcPt3PX8hqZCTrncG8T14UfVH1ZCZBphiJ4WcXG68xb3ig7mvELWvVdOmvSJOx7dURZA1u5de53eFQQGBwvNJ6F5dwywxsK7ZCSlGurACIWdKO2OOroZAjXSrs2Or01tnAQdCirWvK02ZBQIjbpaoMFgybL1jJgap9zbHuFRjtxf238ZD",
            "1204880079680051",
            "106412340868788",
            null,
            null,
            null,
            null,
            null,
            null,
            null
        )                                                                                           //Hardcoded token?

        private fun cardFromResp(j: JSONObject): Card {
            /* TODO
        *   Support Images, Link, Videos and Album.
        *  */
            var c = Card()

            c.id = j.getString("id").split('_')[1].toLong()
            c.timestamp = timestampFromString(j.getString("updated_time"))
            c.body = j.getString("message").replace(Regex.fromLiteral("(\\r|\\n|\\r\\n)+"), "\\\\n")

            if (j.has("message_tags")) {
                var tags = j.getJSONArray("message_tags")
                c.body += "\n"
                for (i in 0 until tags.length()) {
                    if (!tags.getJSONObject(i).has("type")) {
                        c.body += tags.getJSONObject(i).getString("name")
                    }
                }
            }

            if (j.has("attachments")) {
                var dataArr = j.getJSONObject("attachments").getJSONArray("data")
                for (i in 0 until dataArr.length()) {
                    var data = dataArr.getJSONObject(i)
                    when (data.getString("media_type")) {
                        "link" -> {

                        }
                        "video" -> {
                            c.videos.add(data.getJSONObject("media").getString("source"))
                        }
                        "photo" -> {
                            c.images.add(
                                data.getJSONObject("media").getJSONObject("image").getString(
                                    "src"
                                )
                            )
                        }
                        "album" -> {
                            var dataNested =
                                data.getJSONObject("subattachments").getJSONArray("data")
                            for (k in 0 until dataNested.length()) {
                                c.images.add(
                                    dataNested.getJSONObject(k).getJSONObject("media").getJSONObject(
                                        "image"
                                    ).getString("src")
                                )
                            }
                        }
                    }
                }
            }
            return c
        }

        fun getLatestUpdateTime(): Long {
            val request = GraphRequest.newMeRequest(
                accessToken
            ) { `object`, response ->
            }

            val parameters = Bundle()
            parameters.putString("fields", "posts.limit(1){updated_time}")
            request.parameters = parameters

            var resp = request.executeAndWait()
            var ts = resp.jsonObject.getJSONObject("posts").getJSONArray("data").getJSONObject(0)
                .getString("updated_time")

            return timestampFromString(ts)

        }

        fun getPosts(t: Long): MutableList<Card> {
            /* TODO
        *  Handle connection interrupts and other cases
        *  */
            var cl = mutableListOf<Card>()
            var request = GraphRequest.newGraphPathRequest(
                accessToken,
                "/me/posts"
            ) {
                // Insert your code here
            }

            val parameters = Bundle()
            parameters.putString(
                "fields",
                "id,attachments{subattachments,media,media_type,url},message_tags,message,updated_time"
            )
            parameters.putString("limit", "10")
            request.parameters = parameters

            do {
                var resp = request.executeAndWait()

                if (resp == null) {
                    return mutableListOf()
                }

                var posts = resp.jsonObject.getJSONArray("data")
                for (i in 0 until posts.length()) {
                    var c = cardFromResp(posts.getJSONObject(i))
                    if (c.timestamp < t) return cl
                    else cl.add(c)
                }
                request = resp.getRequestForPagedResults(GraphResponse.PagingDirection.NEXT)
            } while (request != null)
            return cl
        }
    }
}