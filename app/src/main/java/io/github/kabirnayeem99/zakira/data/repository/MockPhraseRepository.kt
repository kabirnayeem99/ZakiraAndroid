//package io.github.kabirnayeem99.zakira.data.repository
//
//import io.github.kabirnayeem99.zakira.core.Resource
//import io.github.kabirnayeem99.zakira.domain.entity.Category
//import io.github.kabirnayeem99.zakira.domain.entity.Phrase
//import io.github.kabirnayeem99.zakira.domain.repository.PhraseRepository
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.flow
//import javax.inject.Inject
//
//class MockPhraseRepository @Inject constructor() : PhraseRepository {
//
//    // Sample data for categories and phrases
//    private val categories = mutableListOf(
//        Category(id = 1, name = "Greetings", description = "Common greetings in Arabic."), Category(
//            id = 2, name = "Common Phrases", description = "Everyday phrases used in conversation."
//        )
//    )
//
//    private val phrases = mutableListOf(
//        Phrase(
//            id = 1,
//            arabicWithTashkeel = "السلام عليكم",
//            arabicWithOutTashkeel = "السلام عليكم",
//            meaning = "Peace be upon you",
//            categoryId = 1
//        ),
//        Phrase(
//            id = 2,
//            arabicWithTashkeel = "كيف حالك؟",
//            arabicWithOutTashkeel = "كيف حالك؟",
//            meaning = "How are you?",
//            categoryId = 1
//        ),
//        Phrase(
//            id = 3,
//            arabicWithTashkeel = "هذا حدث، ثم حدث هذا.",
//            arabicWithOutTashkeel = "هذا حدث ثم حدث هذا.",
//            meaning = "This happened, and then this happened.",
//            categoryId = 2
//        ),
//        Phrase(
//            id = 4,
//            arabicWithTashkeel = "عمل جيد. استمر في ذلك.",
//            arabicWithOutTashkeel = "عمل جيد استمر في ذلك.",
//            meaning = "Good work. Keep it up.",
//            categoryId = 2
//        ),
//        Phrase(
//            id = 5,
//            arabicWithTashkeel = "ماذا يجب أن أفعل الآن؟",
//            arabicWithOutTashkeel = "ماذا يجب أن أفعل الآن؟",
//            meaning = "What should I do now?",
//            categoryId = 2
//        ),
//        Phrase(
//            id = 6,
//            arabicWithTashkeel = "يجب أن أذهب إلى المستشفى الآن.",
//            arabicWithOutTashkeel = "يجب أن أذهب إلى المستشفى الآن.",
//            meaning = "I should go to the hospital now.",
//            categoryId = 2
//        ),
//        Phrase(
//            id = 7,
//            arabicWithTashkeel = "دعني أفكر في الأمر.",
//            arabicWithOutTashkeel = "دعني أفكر في الأمر.",
//            meaning = "Let me think about it.",
//            categoryId = 2
//        ),
//        Phrase(
//            id = 8,
//            arabicWithTashkeel = "كيف يمكنني مساعدتك؟",
//            arabicWithOutTashkeel = "كيف يمكنني مساعدتك؟",
//            meaning = "How can I help you?",
//            categoryId = 2
//        ),
//        Phrase(
//            id = 9,
//            arabicWithTashkeel = "هل يمكنك توضيح ذلك؟",
//            arabicWithOutTashkeel = "هل يمكنك توضيح ذلك؟",
//            meaning = "Can you clarify that?",
//            categoryId = 2
//        ),
//        Phrase(
//            id = 10,
//            arabicWithTashkeel = "أنا سعيد برؤيتك.",
//            arabicWithOutTashkeel = "أنا سعيد برؤيتك.",
//            meaning = "I am happy to see you.",
//            categoryId = 2
//        )
//    )
//
//    override fun getPhrases(page: Int, categoryIds: List<Long>): Flow<Resource<List<Phrase>>> {
//        TODO("Not yet implemented")
//    }
//
//    override fun addPhrase(
//        arabicText: String, meaning: String, category: String
//    ): Flow<Resource<Phrase>> = flow {
//        emit(Resource.Loading())
//        val newId = (phrases.maxOfOrNull { it.id } ?: 0) + 1
//        val newPhrase = Phrase(
//            id = newId,
//            arabicWithTashkeel = arabicText,
//            arabicWithOutTashkeel = arabicText,
//            meaning = meaning,
//            categoryId = 1,
//        )
//        phrases.add(newPhrase)
//        emit(Resource.Success(newPhrase)) // Assuming no return value for success
//    }
//
//    override fun deletePhrase(id: Long): Flow<Resource<Phrase>> = flow {
//        emit(Resource.Loading())
//        val phraseToRemove = phrases.find { it.id == id }
//        if (phraseToRemove != null) {
//            phrases.remove(phraseToRemove)
//            emit(Resource.Success(phraseToRemove))
//        } else {
//            emit(Resource.Error("Phrase not found"))
//        }
//    }
//
//    override fun updatePhrase(id: Long, arabic: String, meaning: String): Flow<Resource<Phrase>> =
//        flow {
//            emit(Resource.Loading())
//            var phraseToUpdate = phrases.find { it.id == id }
//            if (phraseToUpdate != null) {
//                phrases.remove(phraseToUpdate)
//                phraseToUpdate = phraseToUpdate.copy(
//                    arabicWithTashkeel = arabic,
//                    meaning = meaning,
//                )
//                phrases.add(phraseToUpdate)
//                emit(Resource.Success(phraseToUpdate))
//            } else {
//                emit(Resource.Error("Phrase not found"))
//            }
//        }
//
//    override fun searchPhrases(
//        query: String,
//        page: Int,
//        categoryIds: List<Long>
//    ): Flow<Resource<List<Phrase>>> {
//        TODO("Not yet implemented")
//    }
//
//
//    override fun getCategories(): Flow<Resource<List<Category>>> = flow {
//        emit(Resource.Loading())
//        emit(Resource.Success(categories))
//    }
//}
