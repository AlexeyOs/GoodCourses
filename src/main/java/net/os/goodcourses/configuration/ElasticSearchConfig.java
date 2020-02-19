package net.os.goodcourses.configuration;

import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.node.Node;
//import org.elasticsearch.node.NodeBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import java.net.InetAddress;
import java.net.UnknownHostException;

@Configuration
@EnableElasticsearchRepositories("net.os.goodcourses.repository.search")
public class ElasticSearchConfig {
	
	@Value("${elasticsearch.home}")
	private String elasticSearchHome;

	@Value("${elasticsearch.cluster.name:elasticsearch}")
	private String clusterName;
	
	/**
	 * http://docs.spring.io/autorepo/docs/spring/4.2.5.RELEASE/spring-framework-reference/html/beans.html
	 * 
	 * By default, beans defined using Java config that have a public close or shutdown method are automatically enlisted with a destruction callback.
	 */
//	@Bean(/*destroyMethod="close"*/)
//	public Node node(){
//		return new NodeBuilder()
//				.local(true)
//				.settings(Settings.builder().put("path.home", elasticSearchHome))
//				.node();
//	}
	@Bean
	public Client client() {
		TransportClient client = null;
		try {
			final Settings elasticsearchSettings = Settings.builder()
					.put("client.transport.sniff", true)
					.put("path.home", elasticSearchHome)
					.put("cluster.name", clusterName).build();
			client = new PreBuiltTransportClient(elasticsearchSettings);
			client.addTransportAddress(new TransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return client;
	}
	
	@Bean
    public ElasticsearchOperations elasticsearchTemplate() {
        return new ElasticsearchTemplate(client());
    }
}
