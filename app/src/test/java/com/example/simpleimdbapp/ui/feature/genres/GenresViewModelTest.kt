import com.example.simpleimdbapp.MainDispatcherRule
import com.example.simpleimdbapp.data.api.imdb.GetGenresApiResponse
import com.example.simpleimdbapp.data.repository.imdb.ImdbRepository
import com.example.simpleimdbapp.domain.model.ApiResponse
import com.example.simpleimdbapp.domain.model.imdb.Genre
import com.example.simpleimdbapp.ui.feature.genres.GenresViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class GenresViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val imdbRepository: ImdbRepository = mockk<ImdbRepository>(relaxed = true)

    private lateinit var viewModel: GenresViewModel

    @Before
    fun setup() {
        viewModel = GenresViewModel(imdbRepository)
    }

    @Test
    fun `getGenres success`() {
        val testData = GetGenresApiResponse(
            genres = listOf(
                Genre(
                    id = 1, "Action",
                )
            )
        )

        coEvery { imdbRepository.getGenres() } returns flow {
            emit(ApiResponse.Success(testData))
        }

        runTest {
            assert(viewModel.genres.value is ApiResponse.Loading)

            viewModel.getGenres()

            assert(viewModel.genres.value is ApiResponse.Success)
            assertEquals(
                (viewModel.genres.value as ApiResponse.Success).data.genres[0].name,
                "Action"
            )
        }
    }

    @Test
    fun `getGenres error`() {
        coEvery { imdbRepository.getGenres() } returns flow {
            emit(ApiResponse.Error("Error message"))
        }

        runTest {
            assert(viewModel.genres.value is ApiResponse.Loading)

            viewModel.getGenres()

            assert(viewModel.genres.value is ApiResponse.Error)
            assertEquals(
                (viewModel.genres.value as ApiResponse.Error).errorMessage,
                "Error message"
            )
        }
    }
}