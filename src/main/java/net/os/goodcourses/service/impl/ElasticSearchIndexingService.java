package net.os.goodcourses.service.impl;

import javax.annotation.PostConstruct;

import net.os.goodcourses.repository.search.ProfileSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Service;

import net.os.goodcourses.entity.Profile;
import net.os.goodcourses.service.FindProfileService;

/**
 *
 */
@Service
public class ElasticSearchIndexingService {
	private static final Logger LOGGER = LoggerFactory.getLogger(ElasticSearchIndexingService.class);

	@Value("${index.all.during.startup}")
	private boolean indexAllDuringStartup;

	@Autowired(required = false)
	private ProfileSearchRepository profileSearchRepository;

	@Autowired
	private ElasticsearchOperations elasticsearchOperations;

	@Autowired
	private FindProfileService findProfileService;

	@PostConstruct
	private void postConstruct() {
		if (indexAllDuringStartup) {
			LOGGER.info("Detected index all command");
			LOGGER.info("Clear old index");
			elasticsearchOperations.deleteIndex(Profile.class);
			LOGGER.info("Start index of profiles");
			for (Profile p : findProfileService.findAllForIndexing()) {
				profileSearchRepository.save(p);
				LOGGER.info("Successful indexing of profile: " + p.getUid());
			}
			LOGGER.info("Finish index of profiles");
		} else {
			LOGGER.info("indexAllDuringStartup is disabled");
		}
	}
}
