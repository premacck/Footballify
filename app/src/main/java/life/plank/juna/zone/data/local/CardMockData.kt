package life.plank.juna.zone.data.local

import life.plank.juna.zone.data.model.card.*
import life.plank.juna.zone.util.common.AppConstants.CardNotificationType.PUBLISHED
import java.util.*

//TODO: Remove this class after Cards integration is complete
object CardMockData {

    private fun selfCardTemplate(cardColor: String, profilePic: String, cardPic: String) = JunaCardTemplate(
            "junaCardTemplateDemo1",
            cardColor,
            Date(),
            PUBLISHED,
            CardUser(
                    "cardUserDemo1",
                    "tealowiz",
                    "Prem Suman",
                    profilePic,
                    cardPic,
                    20,
                    16
            )
    )

    val mockedCardTemplates = mutableListOf(
            selfCardTemplate(
                    "BLUE",
                    "https://scontent.fblr2-1.fna.fbcdn.net/v/t1.0-9/13669760_1209089775791917_924505314148397350_n.jpg?_nc_cat=107&_nc_ht=scontent.fblr2-1.fna&oh=9b8bcb7a6f99a61a2835487901527547&oe=5CB5AD58",
                    "https://scontent.fblr2-1.fna.fbcdn.net/v/t1.0-9/13781916_1209089789125249_8136444602034582782_n.jpg?_nc_cat=108&_nc_ht=scontent.fblr2-1.fna&oh=9ea54c37ae0999a1dc1513327e733d62&oe=5CEEDB51"
            ),
            JunaCardTemplate(
                    "junaCardTemplateDemo2",
                    "BRONZE",
                    Date(),
                    PUBLISHED,
                    CardUser(
                            "cardUserDemo2",
                            "sajadBhai",
                            "Sajad Ahengar",
                            "https://media.licdn.com/dms/image/C5103AQGJ9BjHw1-Q8A/profile-displayphoto-shrink_200_200/0?e=1551916800&v=beta&t=jZ8izQN5cjJj0FzI1s29f7l47OIDWzxIg4y0eR665X8",
                            "data:image/jpeg;base64,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",
                            20,
                            16
                    )
            ),
            JunaCardTemplate(
                    "junaCardTemplateDemo3",
                    "SILVER",
                    Date(),
                    PUBLISHED,
                    CardUser(
                            "cardUserDemo3",
                            "johnTheDoe",
                            "John Doe",
                            "http://dnadoeproject.org/wp-content/uploads/2018/09/John-Clinton-Doe-high-res-1080x1453.jpg",
                            "http://media.graytvinc.com/images/810*455/john+doe+4+for+web.jpg",
                            20,
                            16
                    )
            ),
            JunaCardTemplate(
                    "junaCardTemplateDemo4",
                    "GOLD",
                    Date(),
                    PUBLISHED,
                    CardUser(
                            "cardUserDemo4",
                            "janeDaDoe",
                            "Jane Doe",
                            "https://static01.nyt.com/images/2016/04/28/us/28xp-manson_web1/28xp-manson_web1-jumbo.jpg",
                            "https://upload.wikimedia.org/wikipedia/en/1/13/Woodlawn2016.jpg",
                            20,
                            16
                    )
            ),
            JunaCardTemplate(
                    "junaCardTemplateDemo5",
                    "BLACK",
                    Date(),
                    PUBLISHED,
                    CardUser(
                            "cardUserDemo5",
                            "wowzy65",
                            "Sam Sparks",
                            "https://images-wixmp-ed30a86b8c4ca887773594c2.wixmp.com/intermediary/f/3660d737-b4db-4721-b688-f2824737beba/d6tqm05-efd430bb-f777-4092-8316-38af90aee0d5.png",
                            "https://vignette.wikia.nocookie.net/cloudywithachanceofmeatballs/images/6/69/Sam_%281%29.jpg",
                            20,
                            16
                    )
            )
    )

    val mockedCards = mutableListOf(
            JunaCard(
                    "junaCardDemo1",
                    Date(),
                    mockedCardTemplates[0]
            ),
            JunaCard(
                    "junaCardDemo2",
                    Date(),
                    mockedCardTemplates[1]
            ),
            JunaCard(
                    "junaCardDemo3",
                    Date(),
                    mockedCardTemplates[2]
            ),
            JunaCard(
                    "junaCardDemo4",
                    Date(),
                    mockedCardTemplates[3]
            ),
            JunaCard(
                    "junaCardDemo5",
                    Date(),
                    mockedCardTemplates[4]
            )
    )

    val mockedSelfCards = mutableListOf(
            JunaCard(
                    "junaSelfCardDemo1",
                    Date(),
                    selfCardTemplate(
                            "BLUE",
                            "https://scontent.fblr2-1.fna.fbcdn.net/v/t1.0-9/13669760_1209089775791917_924505314148397350_n.jpg?_nc_cat=107&_nc_ht=scontent.fblr2-1.fna&oh=9b8bcb7a6f99a61a2835487901527547&oe=5CB5AD58",
                            "https://scontent.fblr2-1.fna.fbcdn.net/v/t1.0-9/13781916_1209089789125249_8136444602034582782_n.jpg?_nc_cat=108&_nc_ht=scontent.fblr2-1.fna&oh=9ea54c37ae0999a1dc1513327e733d62&oe=5CEEDB51"
                    )
            ),
            JunaCard(
                    "junaSelfCardDemo2",
                    Date(),
                    selfCardTemplate(
                            "BRONZE",
                            "https://scontent.fblr2-1.fna.fbcdn.net/v/t1.0-9/15672701_1367320989968794_2397528745137876253_n.jpg?_nc_cat=109&_nc_ht=scontent.fblr2-1.fna&oh=499ded6ad1c2395ae4065592105a1ae8&oe=5CF5F589",
                            "https://scontent.fblr2-1.fna.fbcdn.net/v/t1.0-9/15622443_1367313526636207_616270989094889871_n.jpg?_nc_cat=105&_nc_ht=scontent.fblr2-1.fna&oh=2eb6194a9e5dd9eba5d6af6a35f11367&oe=5CEC8B01"
                    )
            ),
            JunaCard(
                    "junaSelfCardDemo3",
                    Date(),
                    selfCardTemplate(
                            "SILVER",
                            "https://scontent.fblr2-1.fna.fbcdn.net/v/t31.0-8/11741208_988195444548019_2128047844968986324_o.jpg?_nc_cat=110&_nc_ht=scontent.fblr2-1.fna&oh=0dc76c64cac404ad4f694ae46ac493b0&oe=5CB8EC02",
                            "https://scontent.fblr2-1.fna.fbcdn.net/v/t31.0-8/13680386_1208185835882311_4573026245977267122_o.jpg?_nc_cat=104&_nc_ht=scontent.fblr2-1.fna&oh=ba7e24fc50b778d7f74d46182368aa0a&oe=5CB7C33A"
                    )
            ),
            JunaCard(
                    "junaSelfCardDemo4",
                    Date(),
                    selfCardTemplate(
                            "GOLD",
                            "https://scontent.fblr2-1.fna.fbcdn.net/v/t1.0-9/10256085_906417539392477_9136246767448226695_n.jpg?_nc_cat=100&_nc_ht=scontent.fblr2-1.fna&oh=f4b3e7538e7bef3e69f4214044bcb0f9&oe=5CBEE456",
                            "https://scontent.fblr2-1.fna.fbcdn.net/v/t1.0-9/13754135_1209087289125499_5506379825485637613_n.jpg?_nc_cat=107&_nc_ht=scontent.fblr2-1.fna&oh=3555520f13e1dacb24267c97fdd3ed53&oe=5CBB7BB8"
                    )
            ),
            JunaCard(
                    "junaSelfCardDemo5",
                    Date(),
                    selfCardTemplate(
                            "BLACK",
                            "https://scontent.fblr2-1.fna.fbcdn.net/v/t1.0-9/45711216_1997573893670481_5565368701751918592_n.jpg?_nc_cat=106&_nc_ht=scontent.fblr2-1.fna&oh=5877352da3820417a37eaf67d0940f32&oe=5CBE38DD",
                            "https://scontent.fblr2-1.fna.fbcdn.net/v/t1.0-9/36717316_1384044591697954_4218660325743919104_n.jpg?_nc_cat=102&_nc_ht=scontent.fblr2-1.fna&oh=661494957d7edfab0abc3ca74027ff71&oe=5CED5AC8"
                    )
            )
    )

    val randomMockedCardTemplate: JunaCardTemplate
        get() = mockedCardTemplates[Random().nextInt(mockedCardTemplates.size)]
}