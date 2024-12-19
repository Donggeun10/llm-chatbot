package com.example.chatbot.configuration;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.document.parser.apache.pdfbox.ApachePdfBoxDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentByLineSplitter;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.input.PromptTemplate;
import dev.langchain4j.model.ollama.OllamaEmbeddingModel;
import dev.langchain4j.rag.DefaultRetrievalAugmentor;
import dev.langchain4j.rag.RetrievalAugmentor;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.rag.query.Query;
import dev.langchain4j.rag.query.router.QueryRouter;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import java.nio.file.FileSystems;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class RAGConfiguration {

    @Value("${application.pdf}")
    private String pdfPath;

    @Bean
    public EmbeddingStoreContentRetriever contentRetriever(EmbeddingStore<TextSegment> embeddingStore, OllamaEmbeddingModel ollamaEmbeddingModel) {

        return EmbeddingStoreContentRetriever.builder()
                    .displayName("java spring boot cds")
                    .embeddingStore(embeddingStore)
                    .embeddingModel(ollamaEmbeddingModel)
                    .maxResults(2)
                    .minScore(0.6)
                    .build();
    }

    @Bean
    public EmbeddingStore<TextSegment> embeddingStore(List<Document> documents, OllamaEmbeddingModel ollamaEmbeddingModel) {

        InMemoryEmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();

        DocumentByLineSplitter documentSplitter = new DocumentByLineSplitter(1000, 200);

        EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
            .embeddingModel(ollamaEmbeddingModel)
            .embeddingStore(embeddingStore)
            .documentSplitter(documentSplitter)
            .build();

        ingestor.ingest(documents);

        return embeddingStore;
    }

    @Bean
    public List<Document> documents() {

        PathMatcher pathMatcher = FileSystems.getDefault().getPathMatcher("glob:*.pdf");
        List<Document> documents = FileSystemDocumentLoader.loadDocuments(Paths.get(pdfPath, "cds"), pathMatcher, new ApachePdfBoxDocumentParser());
        log.info("Loaded {} documents", documents.size());
        for(Document document : documents) {
            document.metadata().put("feature", "spring boot cds");
        }
        return documents;
    }

    @Bean
    public QueryRouter queryRouter(EmbeddingStoreContentRetriever contentRetriever, ChatLanguageModel ollamaChatModel) {

        Map<ContentRetriever, String> retrieverToDescription = new HashMap<>();
        retrieverToDescription.put(contentRetriever, "spring boot cds");

        return new QueryRouter() {

            private final PromptTemplate promptTemplate = PromptTemplate.from(
                "Is the following query related to java based SpringBoot application implementation with CDS(Class Data Sharing) ? " +
                    "Answer only 'yes', 'no' or 'maybe'. " +
                    "Query: {{it}}"
            );

            @Override
            public Collection<ContentRetriever> route(Query query) {

                Prompt prompt = promptTemplate.apply(query.text());

                AiMessage aiMessage = ollamaChatModel.generate(prompt.toUserMessage()).content();
                log.debug("LLM decided: {}...:{}", aiMessage.text(), query.text());
                if (aiMessage.text().toLowerCase().contains("no")) {
                    return emptyList();
                }

                return singletonList(contentRetriever);
            }
        };
    }

    @Bean
    public RetrievalAugmentor retrievalAugmentor(QueryRouter queryRouter) {

        return DefaultRetrievalAugmentor.builder()
            .queryRouter(queryRouter)
            .build();
    }

}
