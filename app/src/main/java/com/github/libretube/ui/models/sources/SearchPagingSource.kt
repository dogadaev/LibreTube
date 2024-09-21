package com.github.libretube.ui.models.sources

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.github.libretube.api.RetrofitInstance
import com.github.libretube.api.obj.ContentItem
import com.github.libretube.api.obj.StreamItem
import com.github.libretube.db.DatabaseHolder.Database
import com.github.libretube.util.deArrow

class SearchPagingSource(
    private val searchQuery: String,
    private val searchFilter: String
) : PagingSource<String, ContentItem>() {
    override fun getRefreshKey(state: PagingState<String, ContentItem>) = null

    override suspend fun load(params: LoadParams<String>): LoadResult<String, ContentItem> {
        return try {
            val blockedChannels = Database.localBlockDao().getAll()

            val result = (params.key?.let {
                RetrofitInstance.api.getSearchResultsNextPage(searchQuery, searchFilter, it)
            } ?: RetrofitInstance.api.getSearchResults(searchQuery, searchFilter)).let { result ->
                result.copy(
                    items = result.items.filter { contentItem ->
                        if (contentItem.type == StreamItem.TYPE_CHANNEL) {
                            blockedChannels.any { blockedChannel ->
                                blockedChannel.url.contains(contentItem.url)
                            }.not()
                        } else {
                            blockedChannels.any { blockedChannel ->
                                blockedChannel.name == contentItem.uploaderName
                            }.not()
                        }
                    }
                )
            }
            LoadResult.Page(result.items.deArrow(), null, result.nextpage)
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}
