package net.os.goodcourses.service;

import net.os.goodcourses.entity.Profile;

import java.util.Optional;

public interface SocialService<T> {

	Optional<Profile> loginViaSocialNetwork(T model);
}
