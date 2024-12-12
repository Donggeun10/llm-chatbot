package com.example.chatbot.configuration;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.document.parser.apache.pdfbox.ApachePdfBoxDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentByLineSplitter;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.ollama.OllamaEmbeddingModel;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import java.nio.file.FileSystems;
import java.nio.file.PathMatcher;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AssistantConfiguration {

    @Value("${application.pdf}")
    private String pdfPath;

    @Bean
    public ChatMemoryProvider chatMemoryProvider() {
        return memoryId -> MessageWindowChatMemory.withMaxMessages(10);
    }

    @Bean
    public EmbeddingStoreContentRetriever contentRetriever(EmbeddingStore<TextSegment> embeddingStore, OllamaEmbeddingModel ollamaEmbeddingModel) {
        return EmbeddingStoreContentRetriever.builder()
                    .embeddingStore(embeddingStore)
                    .embeddingModel(ollamaEmbeddingModel)
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
        return FileSystemDocumentLoader.loadDocuments(pdfPath, pathMatcher,  new ApachePdfBoxDocumentParser());
    }
}
