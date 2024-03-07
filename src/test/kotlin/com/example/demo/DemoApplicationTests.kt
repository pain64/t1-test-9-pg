package com.example.demo

import com.example.demo.dao.CatCommentsDao
import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.spring.SpringExtension
import io.zonky.test.db.postgres.embedded.EmbeddedPostgres
import io.zonky.test.db.postgres.embedded.FlywayPreparer
import org.flywaydb.core.Flyway
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Profile
import org.springframework.context.event.ContextClosedEvent
import org.springframework.context.event.ContextStartedEvent
import org.springframework.context.event.ContextStoppedEvent
import org.springframework.context.event.EventListener
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import javax.sql.DataSource


// Annotations for Spring web controllers
// https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-controller/ann-requestmapping.html
// https://stackoverflow.com/questions/52728391/run-flyway-migrations-inside-java-code-during-runtime

@SpringBootTest(classes = [DemoApplication::class, DemoApplicationTests.TestConfiguration::class])
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class DemoApplicationTests : FunSpec() {
    @Autowired
    private lateinit var mockMvc: MockMvc
    @Autowired
    private lateinit var catCommentDao: CatCommentsDao

    class TestConfiguration {
        class TestCatsService : CatsService {
            override fun randomCat(tag: String?): CatData? =
                when(tag) {
                    null -> CatData("abcdef", "image/jpeg", listOf("white", "funny"))
                    "black" -> CatData("1234", "image/jpeg", listOf("black"))
                    else -> null
                }
        }

        val embeddedPg = EmbeddedPostgres.start()

        init {
            Flyway.configure()
                .locations("db/")
                .dataSource(embeddedPg.postgresDatabase)
                .load()
                .migrate()

            embeddedPg.postgresDatabase.connection.use { conn ->
                conn.createStatement().execute("""
                    select 1
                """.trimIndent())
            }
        }

        @Bean
        @Profile("test")
        fun catsService() = TestCatsService()

        @Bean
        @Profile("test")
        fun dataSource(): DataSource {
            return embeddedPg.postgresDatabase
        }

        @EventListener
        fun handleContextCloseEvent(closeEvt: ContextClosedEvent) {
            embeddedPg.close()
        }
    }

    init {
        extension(SpringExtension)

        test("random cat no tag") {
            mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/randomCat")
            )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(
                    MockMvcResultMatchers.content().json(
                        """
                    {"id":"abcdef","mimeType":"image/jpeg","tags":["white", "funny"],"comments":[]}
                """
                    )
                )
        }
    }
}
