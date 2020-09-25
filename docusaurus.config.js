module.exports = {
  title: 'Pathom 3',
  tagline: 'Connecting data made easy.',
  url: 'https://your-docusaurus-test-site.com',
  baseUrl: '/',
  onBrokenLinks: 'throw',
  favicon: 'img/favicon.ico',
  organizationName: 'wilkerlucio', // Usually your GitHub org/user name.
  projectName: 'pathom3', // Usually your repo name.
  themeConfig: {
    sidebarCollapsible: false,
    prism: {
      theme: require('prism-react-renderer/themes/dracula'),
      additionalLanguages: ['clojure'],
    },
    navbar: {
      title: 'Pathom 3',
      logo: {
        alt: 'Pathom 3',
        src: 'img/icon_64x64.png',
      },
      items: [
        {
          to: 'docs/introduction',
          activeBasePath: 'docs',
          label: 'Docs',
          position: 'left',
        },
        {to: 'blog', label: 'Blog', position: 'left'},
        {
          href: 'https://github.com/wilkerlucio/pathom3',
          label: 'GitHub',
          position: 'right',
        },
      ],
    },
    footer: {
      style: 'dark',
      links: [
        {
          title: 'Docs',
          items: [
            {
              label: 'Introduction',
              to: 'docs/introduction',
            }
          ],
        },
        {
          title: 'Community',
          items: [
            {
              label: 'Slack',
              href: 'https://clojurians.slack.com/messages/pathom/',
            }
          ],
        }
      ],
      copyright: `Copyright © ${new Date().getFullYear()} Wilker Lucio`,
    },
  },
  presets: [
    [
      '@docusaurus/preset-classic',
      {
        docs: {
          sidebarPath: require.resolve('./sidebars.js'),
          // Please change this to your repo.
          editUrl:
            'https://github.com/wilkerlucio/pathom3-docs/edit/master/',
        },
        blog: {
          showReadingTime: true,
          // Please change this to your repo.
          editUrl:
            'https://github.com/wilkerlucio/pathom3-docs/edit/master/blog/',
        },
        theme: {
          customCss: require.resolve('./src/css/custom.css'),
        },
      },
    ],
  ],
};
