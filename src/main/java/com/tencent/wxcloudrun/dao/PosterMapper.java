package com.tencent.wxcloudrun.dao;



import com.tencent.wxcloudrun.model.Poster;

import java.util.List;

public interface PosterMapper {
    List<Poster> queryPoster(int type);

    List<Poster> getPosters();

    Poster getPoster(int id);

    void deletePoster(int id);

    void deletePosters(List<Integer> ids);

    void updatePoster(Poster Poster);

    void createPoster(Poster poster);
}
