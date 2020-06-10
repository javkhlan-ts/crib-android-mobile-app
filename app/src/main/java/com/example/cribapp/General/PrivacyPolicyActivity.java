package com.example.cribapp.General;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cribapp.R;
import com.example.cribapp.RentAccountSettingsActivity;

public class PrivacyPolicyActivity extends AppCompatActivity {
    private TextView mPrivacyPolicy;
    private ImageView mBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);

        mBack = findViewById(R.id.icon_back);
        mPrivacyPolicy = findViewById(R.id.text_privacy_policy);

        setTextForPrivacyPolicy();
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PrivacyPolicyActivity.this, RentAccountSettingsActivity.class));
            }
        });
    }

    private void setTextForPrivacyPolicy() {
        mPrivacyPolicy.setText("Effective Date: January 1, 2020\n" +
                "This Privacy Notice applies to Personal Information processed by Crib in the course of our business, including, Crib.com, Crib.ca, the Crib Applications, as well as additional sites/applications operated by our affiliates and subsidiaries, including Crib Mortgage, Crib Now, Crib Direct, Walkscore, and Title Forward (each a \"Site\") and the services provided through the Sites (collectively with the Sites, the \"Services\").\n" +
                "The terms \"Crib,\" \"we,\" \"us,\" and \"our\" includes Crib Corporation and our affiliates and subsidiaries (including, without limitation, Crib Unlimited Liability Company for individuals located in Canada).\n" +
                "Please note that unless we define a term in this Privacy Notice, capitalized terms used in this Privacy Notice have the same meanings as in our Terms of Use.\n" +
                "This Privacy Notice contains the following sections:\n" +
                "1. Personal Information Collection and Use\n" +
                "2. Personal Information Sharing\n" +
                "3. Your Choices\n" +
                "4. Individual Rights in Personal Information\n" +
                "5. Personal Information Retention\n" +
                "6. Personal Information Storage and Security\n" +
                "7. Other Important Information\n" +
                "1. Personal Information Collection and Use\n" +
                "This Privacy Notice covers our use of Personal Information. \"Personal Information,\" means any information that relates to you and can -- either on its own or in combination with other information -- identify you as an individual. Personal information does not include information that has been de-identified or aggregated such that you can no longer be identified.\n" +
                "The types of information we collect depends on your level of engagement with the Services. The more you interact with the Services, the more information we need to provide the Services. This Section explains how we collect and use personal information based on your level of engagement with the Services.\n" +
                "A. All Users of the Crib Sites\n" +
                "From registered and unregistered users of the Sites, we automatically receive and record information whenever you interact with the Sites. This includes IP address data from our server logs, cookie information, browser information, information about your activity while on the Sites (such as the pages you request, properties that you view or save, and property that you claim), and the URL of the site you came to the Sites from. We also may collect the mobile device ID (such as the ID for Advertising on iOS and Android ID on Android) from users of the Crib Applications. If permitted by the settings on your device and/or with your consent, we will also collect your geo-location information. We use this to provide you with location specific information, such as homes for sale near your current location as determined by your geolocation, and information about listing your home for sale based on your geolocation. We may also approximate your location based off your IP address, to customize your experience on our Sites.\n" +
                "We may also use the information listed above to help diagnose problems and performance with the Services, analyze trends, customize your experience, and administer the Services. We may provide IP addresses to third-parties to help us analyze website user survey data. We may also use the information described in this section to better target information about Crib's Sites and Services, for example, by displaying more accurate or relevant information on our Sites, matching you to a public record listing of a property, tailoring the advertising we display on third party websites and platforms, and sending better targeted and tailored messages, including via email and/or push notifications.\n" +
                "B. Registered Users of the Crib Sites\n" +
                "We are required to collect certain information, including your name and email address, as a condition of our licenses with the multiple real estate listing services (the \"MLSs\") in order to allow you to access the full MLS listings. You give us this information when you create your Crib account. When you give us this information at one of our events, or upon your request, we will create a Crib account for you, and you agree that this Privacy Notice and our Terms of Use apply. In addition, when you are signed in to your Crib account and using Crib through Facebook, Twitter, or any other third-party authorization service, we may make use of the information that they provide us to further customize your Crib experience. We may share Personal Information with such third parties to deliver Crib tailored content both on our Services and on third party sites (e.g., social media sites).\n" +
                "My Crib: My Crib is a feature that allows you to upload notes and photos about a property, save searches, add favorites and x-outs, and otherwise organize homes that may interest you. We save all of this \"My Crib\" information for use later in connection with our Services. It is stored with your Crib account information.\n" +
                "Account Password Security: Your Crib account password is encrypted on our server. It is your responsibility not to disclose your password to other people and to ensure you maintain its security.\n" +
                "Account Updates: By visiting your My Crib: Account Settings, you can correct, amend, add or delete Personal Information associated with your account. However, even after you update information, we may maintain a copy of the original information in our records as required by applicable law or other legal obligation.\n" +
                "C. Home Tours, Real Estate Classes, and other In-Person Meetings\n" +
                "When you submit a home tour request, we will ask for your tour date and time preferences, as well as your phone number. We may ask for similar information when you request or attend a home buying or selling class, or when you request or attend a strategy session or similar in-person meeting with a Crib agent. We may also ask some questions so that our agent can verify your identity, and such information will be used to register you for a Crib account. When you meet up with one of our agents, the agent may ask you to verify your identity and may even ask to see a photo ID.\n" +
                "D. Crib Clients\n" +
                "When you are getting ready to buy or sell a home and you start working with our coordinators and agents, you become a Crib client. As part of the home purchase or sale process, you will need to provide a lot of information as is customary for a home purchase or sale transaction. This information can include, without limitation, your name, address, phone number, email address, financial or payment information, including without limitation, buyer loan pre-approval documentation and seller property information. We ask you, as a buyer, for loan pre-approvals from a mortgage broker or lender to determine which homes are in your price range and to help you make an offer more quickly when you do find a home. You, as a seller, may be legally required to disclose certain information about your property to prospective buyers. Once you sign a listing agreement or buyer agreement with us, your relationship with Crib is governed by that agreement, as well as our online Terms of Use and this Privacy Notice.\n" +
                "E. Crib Mortgage\n" +
                "For users of Crib Mortgage, the Crib Mortgage Consumer Privacy Notice also applies to the processing of your Personal Information.\n" +
                "F. Communications with Crib\n" +
                "Crib may collect Personal Information from you such as email address, phone number, or mailing address when you request information about our Services, register for our newsletter, request customer or technical support, or otherwise communicate with us.\n" +
                "G. Marketing\n" +
                "We may send marketing materials to Crib account holders and clients using various communication channels, including without limitation, email, text messages/SMS, push notifications, telephone calls, and direct mail. Individuals may also subscribe to Listing Alert Emails or other notifications with information such as customized summaries of homes for sale. Crib may send you Crib-related news and surveys in accordance with applicable law.\n" +
                "H. Call Recording\n" +
                "We may record any telephone calls between you and our agents or other representatives for training and quality assurance purposes.\n" +
                "I. Surveys\n" +
                "Crib may contact you to participate in surveys. If you decide to participate, you may be asked to provide certain information, which may include Personal Information.\n" +
                "J. Sharing with Friends or Colleagues\n" +
                "Crib's Services may allow you to forward or share certain content with a friend or colleague, such as an email inviting your friend to use our Services. Email addresses that you may provide for a friend or colleague will be used to send your friend or colleague the content or link you request in accordance with applicable law.\n" +
                "K. Interactive Features\n" +
                "Crib may offer interactive features such as chat services, forums, and social media pages. We may collect the information you submit or make available through these features. Any content you provide on the public sections of these channels will be considered \"public\" and will not be subject to the privacy protections referenced herein.\n" +
                "L. De-Identified and Aggregated Information\n" +
                "We may use Personal Information and other information about you to create de-identified and/or aggregated information, such as de-identified demographic information, de-identified location information, de-identified or aggregated trends, reports, or statistics, information about the computer or device from which you access our Services, or other analyses we create. De-identified and aggregated information is used for a variety of functions, including the measurement of visitors' interest in and use of various portions or features of the Sites and Services. De-identified and aggregated information is not Personal Information, and we may use and disclose such information in a number of ways, including research, internal analysis, analytics, and any other legally permissible purpose.\n" +
                "M. Conferences and Trade Shows\n" +
                "We may attend or participate in conferences and trade shows where we collect Personal Information from individuals who interact with or express an interest in Crib and/or the Services. If you provide us with any information at one of these events, we will use it for the purposes for which it was collected.\n" +
                "N. Information We Collect Automatically\n" +
                "We may collect certain information automatically when you use the Services. This information may include your Internet protocol (IP) address, user settings, IMEI, MAC address, cookie identifiers, mobile carrier, mobile advertising and other unique identifiers, details about your browser, operating system or device, location information, Internet service provider, pages that you visit before, during and after using the Services, information about the links you click, and other information about how you use the Services. Information we collect may be associated with accounts and other devices.\n" +
                "O. Cookies and Pixel Tags/Web Beacons\n" +
                "We, as well as third parties that provide content, advertising, or other functionality on the Services, may use cookies, pixel tags, local storage, and other technologies (\"Technologies\") to automatically collect information through the Services. Technologies are essentially small data files placed on your computer, tablet, mobile phone, or other devices that allow us to record certain pieces of information whenever you visit or interact with our Sites and Services.\n" +
                "Cookies. Cookies are small text files placed in visitors' device browsers to store their preferences. Most browsers allow you to block and delete cookies. However, if you do that, the Services may not work properly.\n" +
                "Pixel Tags/Web Beacons. A pixel tag (also known as a web beacon) is a piece of code embedded on the Sites that collects information about users' engagement on that web page. The use of a pixel allows us to record, for example, that a user has visited a particular web page or clicked on a particular advertisement.\n" +
                "You may stop or restrict the placement of Technologies on your device or remove them by adjusting your preferences as your browser or device permits.\n" +
                "P. Information from Other Sources\n" +
                "We may obtain information about you from other sources, including through third-party services and organizations to supplement information provided by you. This supplemental information allows us to verify information that you have provided to us and to enhance our ability to provide you with the Services.");
    }
}
