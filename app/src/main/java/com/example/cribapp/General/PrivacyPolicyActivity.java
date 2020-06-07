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
                "\n" +
                "This Privacy Notice applies to Personal Information processed by Redfin in the course of our business, including, Redfin.com, Redfin.ca, the Redfin Applications, as well as additional sites/applications operated by our affiliates and subsidiaries, including Redfin Mortgage, Redfin Now, Redfin Direct, Walkscore, and Title Forward (each a \"Site\") and the services provided through the Sites (collectively with the Sites, the \"Services\").\n" +
                "\n" +
                "The terms \"Redfin,\" \"we,\" \"us,\" and \"our\" includes Redfin Corporation and our affiliates and subsidiaries (including, without limitation, Redfin Unlimited Liability Company for individuals located in Canada).\n" +
                "\n" +
                "Please note that unless we define a term in this Privacy Notice, capitalized terms used in this Privacy Notice have the same meanings as in our Terms of Use.\n" +
                "\n" +
                "This Privacy Notice contains the following sections:\n" +
                "\n" +
                "1. Personal Information Collection and Use\n" +
                "2. Personal Information Sharing\n" +
                "3. Your Choices\n" +
                "4. Individual Rights in Personal Information\n" +
                "5. Personal Information Retention\n" +
                "6. Personal Information Storage and Security\n" +
                "7. Other Important Information\n" +
                "\n" +
                "1. Personal Information Collection and Use\n" +
                "\n" +
                "This Privacy Notice covers our use of Personal Information. \"Personal Information,\" means any information that relates to you and can -- either on its own or in combination with other information -- identify you as an individual. Personal information does not include information that has been de-identified or aggregated such that you can no longer be identified.\n" +
                "\n" +
                "The types of information we collect depends on your level of engagement with the Services. The more you interact with the Services, the more information we need to provide the Services. This Section explains how we collect and use personal information based on your level of engagement with the Services.\n" +
                "\n" +
                "A. All Users of the Redfin Sites\n" +
                "\n" +
                "From registered and unregistered users of the Sites, we automatically receive and record information whenever you interact with the Sites. This includes IP address data from our server logs, cookie information, browser information, information about your activity while on the Sites (such as the pages you request, properties that you view or save, and property that you claim), and the URL of the site you came to the Sites from. We also may collect the mobile device ID (such as the ID for Advertising on iOS and Android ID on Android) from users of the Redfin Applications. If permitted by the settings on your device and/or with your consent, we will also collect your geo-location information. We use this to provide you with location specific information, such as homes for sale near your current location as determined by your geolocation, and information about listing your home for sale based on your geolocation. We may also approximate your location based off your IP address, to customize your experience on our Sites.\n" +
                "\n" +
                "We may also use the information listed above to help diagnose problems and performance with the Services, analyze trends, customize your experience, and administer the Services. We may provide IP addresses to third-parties to help us analyze website user survey data. We may also use the information described in this section to better target information about Redfin's Sites and Services, for example, by displaying more accurate or relevant information on our Sites, matching you to a public record listing of a property, tailoring the advertising we display on third party websites and platforms, and sending better targeted and tailored messages, including via email and/or push notifications.\n" +
                "\n" +
                "B. Registered Users of the Redfin Sites\n" +
                "\n" +
                "We are required to collect certain information, including your name and email address, as a condition of our licenses with the multiple real estate listing services (the \"MLSs\") in order to allow you to access the full MLS listings. You give us this information when you create your Redfin account. When you give us this information at one of our events, or upon your request, we will create a Redfin account for you, and you agree that this Privacy Notice and our Terms of Use apply. In addition, when you are signed in to your Redfin account and using Redfin through Facebook, Twitter, or any other third-party authorization service, we may make use of the information that they provide us to further customize your Redfin experience. We may share Personal Information with such third parties to deliver Redfin tailored content both on our Services and on third party sites (e.g., social media sites).\n" +
                "\n" +
                "My Redfin: My Redfin is a feature that allows you to upload notes and photos about a property, save searches, add favorites and x-outs, and otherwise organize homes that may interest you. We save all of this \"My Redfin\" information for use later in connection with our Services. It is stored with your Redfin account information.\n" +
                "\n" +
                "Account Password Security: Your Redfin account password is encrypted on our server. It is your responsibility not to disclose your password to other people and to ensure you maintain its security.\n" +
                "\n" +
                "Account Updates: By visiting your My Redfin: Account Settings, you can correct, amend, add or delete Personal Information associated with your account. However, even after you update information, we may maintain a copy of the original information in our records as required by applicable law or other legal obligation.\n" +
                "\n" +
                "C. Home Tours, Real Estate Classes, and other In-Person Meetings\n" +
                "\n" +
                "When you submit a home tour request, we will ask for your tour date and time preferences, as well as your phone number. We may ask for similar information when you request or attend a home buying or selling class, or when you request or attend a strategy session or similar in-person meeting with a Redfin agent. We may also ask some questions so that our agent can verify your identity, and such information will be used to register you for a Redfin account. When you meet up with one of our agents, the agent may ask you to verify your identity and may even ask to see a photo ID.\n" +
                "\n" +
                "D. Redfin Clients\n" +
                "\n" +
                "When you are getting ready to buy or sell a home and you start working with our coordinators and agents, you become a Redfin client. As part of the home purchase or sale process, you will need to provide a lot of information as is customary for a home purchase or sale transaction. This information can include, without limitation, your name, address, phone number, email address, financial or payment information, including without limitation, buyer loan pre-approval documentation and seller property information. We ask you, as a buyer, for loan pre-approvals from a mortgage broker or lender to determine which homes are in your price range and to help you make an offer more quickly when you do find a home. You, as a seller, may be legally required to disclose certain information about your property to prospective buyers. Once you sign a listing agreement or buyer agreement with us, your relationship with Redfin is governed by that agreement, as well as our online Terms of Use and this Privacy Notice.\n" +
                "\n" +
                "E. Redfin Mortgage\n" +
                "\n" +
                "For users of Redfin Mortgage, the Redfin Mortgage Consumer Privacy Notice also applies to the processing of your Personal Information.\n" +
                "\n" +
                "F. Communications with Redfin\n" +
                "\n" +
                "Redfin may collect Personal Information from you such as email address, phone number, or mailing address when you request information about our Services, register for our newsletter, request customer or technical support, or otherwise communicate with us.\n" +
                "\n" +
                "G. Marketing\n" +
                "\n" +
                "We may send marketing materials to Redfin account holders and clients using various communication channels, including without limitation, email, text messages/SMS, push notifications, telephone calls, and direct mail. Individuals may also subscribe to Listing Alert Emails or other notifications with information such as customized summaries of homes for sale. Redfin may send you Redfin-related news and surveys in accordance with applicable law.\n" +
                "\n" +
                "H. Call Recording\n" +
                "\n" +
                "We may record any telephone calls between you and our agents or other representatives for training and quality assurance purposes.\n" +
                "\n" +
                "I. Surveys\n" +
                "\n" +
                "Redfin may contact you to participate in surveys. If you decide to participate, you may be asked to provide certain information, which may include Personal Information.\n" +
                "\n" +
                "J. Sharing with Friends or Colleagues\n" +
                "\n" +
                "Redfin's Services may allow you to forward or share certain content with a friend or colleague, such as an email inviting your friend to use our Services. Email addresses that you may provide for a friend or colleague will be used to send your friend or colleague the content or link you request in accordance with applicable law.\n" +
                "\n" +
                "K. Interactive Features\n" +
                "\n" +
                "Redfin may offer interactive features such as chat services, forums, and social media pages. We may collect the information you submit or make available through these features. Any content you provide on the public sections of these channels will be considered \"public\" and will not be subject to the privacy protections referenced herein.\n" +
                "\n" +
                "L. De-Identified and Aggregated Information\n" +
                "\n" +
                "We may use Personal Information and other information about you to create de-identified and/or aggregated information, such as de-identified demographic information, de-identified location information, de-identified or aggregated trends, reports, or statistics, information about the computer or device from which you access our Services, or other analyses we create. De-identified and aggregated information is used for a variety of functions, including the measurement of visitors' interest in and use of various portions or features of the Sites and Services. De-identified and aggregated information is not Personal Information, and we may use and disclose such information in a number of ways, including research, internal analysis, analytics, and any other legally permissible purpose.\n" +
                "\n" +
                "M. Conferences and Trade Shows\n" +
                "\n" +
                "We may attend or participate in conferences and trade shows where we collect Personal Information from individuals who interact with or express an interest in Redfin and/or the Services. If you provide us with any information at one of these events, we will use it for the purposes for which it was collected.\n" +
                "\n" +
                "N. Information We Collect Automatically\n" +
                "\n" +
                "We may collect certain information automatically when you use the Services. This information may include your Internet protocol (IP) address, user settings, IMEI, MAC address, cookie identifiers, mobile carrier, mobile advertising and other unique identifiers, details about your browser, operating system or device, location information, Internet service provider, pages that you visit before, during and after using the Services, information about the links you click, and other information about how you use the Services. Information we collect may be associated with accounts and other devices.\n" +
                "\n" +
                "O. Cookies and Pixel Tags/Web Beacons\n" +
                "\n" +
                "We, as well as third parties that provide content, advertising, or other functionality on the Services, may use cookies, pixel tags, local storage, and other technologies (\"Technologies\") to automatically collect information through the Services. Technologies are essentially small data files placed on your computer, tablet, mobile phone, or other devices that allow us to record certain pieces of information whenever you visit or interact with our Sites and Services.\n" +
                "\n" +
                "Cookies. Cookies are small text files placed in visitors' device browsers to store their preferences. Most browsers allow you to block and delete cookies. However, if you do that, the Services may not work properly.\n" +
                "\n" +
                "Pixel Tags/Web Beacons. A pixel tag (also known as a web beacon) is a piece of code embedded on the Sites that collects information about users' engagement on that web page. The use of a pixel allows us to record, for example, that a user has visited a particular web page or clicked on a particular advertisement.\n" +
                "\n" +
                "You may stop or restrict the placement of Technologies on your device or remove them by adjusting your preferences as your browser or device permits.\n" +
                "\n" +
                "P. Information from Other Sources\n" +
                "\n" +
                "We may obtain information about you from other sources, including through third-party services and organizations to supplement information provided by you. This supplemental information allows us to verify information that you have provided to us and to enhance our ability to provide you with the Services.\n" +
                "\n" +
                "Q. Other Uses of Information\n" +
                "\n" +
                "In addition to the specific collection and uses listed above, we may use any of the Personal Information we collect for the following business purposes:\n" +
                "\n" +
                "to operate our Sites and provide our Services in accordance with our Terms of Use and/or any other agreement you may have with us;\n" +
                "to manage your Personal Information and accounts;\n" +
                "to respond to your questions or inquiries;\n" +
                "to conduct promotional activities, including contests or surveys;\n" +
                "to undertake internal research for technological development and demonstration;\n" +
                "to improve, upgrade or enhance our Sites and Services;\n" +
                "to send you informational or promotional communications;\n" +
                "to detect bugs, security incidents (including to protect against malicious, deceptive, fraudulent, or illegal activity, and prosecute those responsible for that activity);\n" +
                "to identify and repair errors that impair existing intended functionality;\n" +
                "to undertake activities to verify or maintain the quality or safety of a Service, Site or transaction;\n" +
                "to carry out other purposes that are disclosed to you and/or to which you consent; and\n" +
                "to pursue our legitimate interests where your rights and freedoms do not outweigh these interests.");
    }
}
