package workshop.rag;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Configuration
@Slf4j
public class RagConfiguration {
//    Retrieval Augmented generation Configuration file

    @Value("classpath:/data/models.json")
    private Resource models;

    @Bean
    SimpleVectorStore simpleVectorStore(EmbeddingModel embeddingModel) {
//        build up simple vector store with an embedding model
        SimpleVectorStore vectorStore = SimpleVectorStore.builder(embeddingModel).build();

//        get source file that acts as database for storing the vectors. vector store
        File sourceFile = this.getDataSourceFile();

        if (sourceFile.exists()) {
            log.info("source file already exists at {} ", sourceFile.getAbsolutePath());
            vectorStore.load(sourceFile);
        } else {
            log.info("creating vector store at {} ", sourceFile.getAbsolutePath());

//            use a text reader to read the file containing data
            TextReader textReader = new TextReader(models);
            textReader.getCustomMetadata().put("filename", "models.txt");

//            put the read data from the file into Documents ( container for document metadata and contents)
            List<Document> documents = textReader.get();
            TokenTextSplitter tokenTextSplitter = new TokenTextSplitter();
            List<Document> splitDocuments = tokenTextSplitter.apply(documents);

            vectorStore.add(splitDocuments);
            vectorStore.save(sourceFile);

        }
        return vectorStore;
    }

    private File getDataSourceFile() {
        Path path = Paths.get("src", "main", "resources", "data");
        String storage = "vectorstore.json";
        String absolutePath = path.toFile().getAbsolutePath() + "/" + storage;
        return new File(absolutePath);
    }
}
