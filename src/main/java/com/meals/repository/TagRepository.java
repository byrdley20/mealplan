package com.meals.repository;

import com.meals.domain.Tag;

public interface TagRepository extends BaseRepository<Tag> {

	public Tag findByName(String name);
}
