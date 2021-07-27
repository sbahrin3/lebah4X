/* ************************************************************************
LEBAH PORTAL FRAMEWORK, http://lebah.sf.net
Copyright (C) 2007  Shamsul Bahrin








MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.

* ************************************************************************ */
package lebah.portal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */
public class SectionInfoModule extends lebah.portal.velocity.VTemplate {

	String section = "";
	
	public SectionInfoModule(VelocityEngine engine, VelocityContext context, HttpServletRequest req, HttpServletResponse res) {
		super(engine, context, req, res);
	}
	
	public void setSection(String s) {
		section = s;
	}
	
	public Template doTemplate() throws Exception {
		
		context.put("section", section);
		
		Template template = engine.getTemplate("vtl/sis/web/sectioninfo.vm");	
		return template;		
	}
}
